(ns clojure-noob.ch9
  (:require [clojure.string :as str]
            [clojure.repl :as repl])
  (:import (java.net URLEncoder))
  (:gen-class))

;; Concurrency - managing more than one task at the same time
;; Parallelism - executing more than one task at the same time

(let [result (future (println "this prints once")
                     (+ 1 1))]
  (println "deref: " (deref result))
  (println "@: " @result))

;; Once a future's body has been executed once, the result is cached
;; on next deref, the println won't execute

;; derefing a future blocks if the future hasn't finished running

;; place a time limit on how long to wait for a future

(deref (future (Thread/sleep 1000) 0) 10 5) ;; returns 10 if future doesn't return a value within 10 milliseconds

;; ([ref] [ref timeout-ms timeout-val])

(realized? (future (Thread/sleep 1000)))

(let [f (future)]
  @f
  (realized? f))
;; => true

;; future - chuck tasks onto other threads
;; Clojure allows you to treat task defn and requiring the result independently with delays and promises

;; First Concurrency Goblin
;; reference cell problem - occurs when two threads can read and write to the same location and the value of the location depends on the order of the reads and writes

;; Second Concurrency Goblin
;; Mutual Exclusion - each thread is trying to write something to file but doesn't have exclusive write access. the output ends up being garbled because the writes are interleaved

;; Third Concurrency Goblin
;; deadlock - each thread blocks indefinitely for a resource to become available

;; 3 events - Task Defn, Task Execution and Requiring a Task's result

;; Delays - define a task without having to execute it or require the result indefinitely

(def jackson-5-delay
  (delay (let [message "Just call my name and I'll be there"]
           (println "First deref: " message)
           message)))

;; evaluate the delay and get result by deref or force

;; force is identical to deref but communicates the intent more clearly
;; causing a task to start as opposed to waiting for it to finish
(force jackson-5-delay)
;; => "First deref: Just call my name and I'll be there"
;; => "Just call my name and I'll be there"

@jackson-5-delay
;; => "Just call my name and I'll be there"
;; One way you can use a delay is to fire off a statement the first time one future out of a group of related futures finishes

(def gimli-headshots ["serious.jpg" "fun.jpg" "playful.jpg"])
(defn email-user
  [email-address]
  (println "Sending headshot notification to" email-address))
(defn upload-document
  "Needs to be implemented"
  [headshot]
  true)
(let [notify (delay (email-user "and-my-axe@gmail.com"))]
  (doseq [headshot gimli-headshots]
    (future (upload-document headshot)
            (force notify))))

;; Promises allow you to express that you expect a result without having to define the task that should produce it or when that task should run

(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise

;; create a promise and deliver a value to it
;; Can only deliver a value to a promise once

(def yak-butter-international
  {:store "Yak Butter International"
   :price 90
   :smoothness 90})
(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})
;; This is the butter that meets our requirements
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "if the butter meets our criteria, return it, else return false"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

;; Happens synchronously. Takes about 3 seconds
(time (some (comp satisfactory? mock-api-call)
            [yak-butter-international butter-than-nothing baby-got-yak]))

(time
 (let [butter-promise (promise)]
   (doseq [butter [yak-butter-international butter-than-nothing baby-got-yak]]
     (future (if-let [satisfactory-butter (satisfactory? (mock-api-call butter))]
               (deliver butter-promise satisfactory-butter))))
   (println "And the winner is: " @butter-promise)))
;; And the winner is:  {:store Baby Got Yak, :price 94, :smoothness 99}
;; "Elapsed time: 1003.752965 msecs"
;; Decouples the requirement for a result from how the result from how the result is actually computed. Can perform multiple computations in parallel.

(let [p (promise)]
  (deref p 100 "timed out"))
;; => "timed out"

(let [ferengi-wisdom-promise (promise)]
  (future (println "here's some ferengi wisdom: " @ferengi-wisdom-promise))
  (Thread/sleep 100)
  (deliver ferengi-wisdom-promise "WHisper your way to success."))
;; => #<Promise@3edac563: "WHisper your way to success.">
;; here's some ferengi wisdom:  WHisper your way to success.

;; Rolling your own Q

(defmacro wait
  "Sleep `timeout` seconds before evaluating the body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(wait 200 (println "Bippity Boppity Boo"))

(let [saying3 (promise)]
  (future (deliver saying3 (wait 100 "Cheerio")))
  @(let [saying2 (promise)]
     (future (deliver saying2 (wait 400 "Pip pip!")))
     @(let [saying1 (promise)]
        (future (deliver saying1 (wait 200 "Ello, gov'na!")))
        (println @saying1)
        saying1)
     (println @saying2)
     saying2)
  (println @saying3)
  saying3)

(defmacro enqueue
  ([q concurrent-promise-name concurrent serialized]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialized
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent serialized]
   ~(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))

;; Chapter Exercises
;; 1. Write a function that takes a string as an argument and searches for it on Bing and Google using the slurp function. Your function should return the HTML of the first page returned by the search.

(defmacro ustr
  "URL Encodes the string"
  [& s]
  `(URLEncoder/encode (str ~@s)))

(defn construct-search-url
  "Given the name of a search engine, Constructs a valid search URL"
  [engine]
  (str "https://" engine ".com/search?q%3D")) ;; Have to url encode the space

(defn search-on-web
  [search-string]
  (let [encoded-search (ustr search-string)
        first-page (promise)]
    (doseq [x ["bing" "google"]]
      (let [search-url (str (construct-search-url x) encoded-search)]
        (future (deliver first-page {:page (slurp search-url) :engine x}))))
    (deref first-page)))

(def srch (search-on-web "athens"))

;; 2. Update your function so it takes a second argument consisting of the search engines to use.

(defn search-on-web
  [search-string engines]
  (let [encoded-search (ustr search-string)
        first-page (promise)]
    (doseq [x engines]
      (let [search-url (str (construct-search-url x) encoded-search)]
        (future (deliver first-page {:page (slurp search-url) :engine x}))))
    (deref first-page)))

(def srch (search-on-web "I am a doo doo head" ["bing" "google"]))
(:engine srch)
;; => "google"

;; 3. Create a new function that takes a search term and search engines as arguments, and returns a vector of the URLs from the first page of search results from each search engine.

(def url-regex #"[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)")

(defn first-page-urls
  "Takes a search term, search engines and returns a vector of URLS from first page from each search engine"
  [search-string engines]
  (let [encoded-search (ustr search-string)
        first-page-urls (atom {})]
    (doseq [x engines]
      (let [search-url (str (construct-search-url x) encoded-search)]
        (future ())))
    first-page-urls))
