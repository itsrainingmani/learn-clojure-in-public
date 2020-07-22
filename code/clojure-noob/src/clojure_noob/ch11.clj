(ns clojure-noob.ch11
  (:require [clojure.repl :refer :all])
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]])
  (:gen-class))

;; ch 11 core.async

;; Process - a concurrently running unit of logic that responds to events

(def echo-chan (chan))        ;; create a channel

;; go block runs concurrently on a separate thread
;; Go blocks run processes on a thread pool that contains a number of threads
;; equal to two plus the number of cores on your machine.

;; <! take function - listens to the channel and the process it belongs to waits until another process puts a message in the channel
(go (println (<! echo-chan))) ;; create a new process

;; >!! puts a value in the given channel and returns true
;; when you put a message on a channel, process blocks until another process takes the message
(>!! echo-chan "ketchup")

;; processes wait both for receiving messages and wait for the messages on a channel to be taken

;; Channels communicate messages
;; You can put messages on a channel and take messages from a channel
;; Processes wait for the completion of put and take.

(def echo-buffer (chan 2))  ;; buffered channel
(>!! echo-buffer "ketchup")
(>!! echo-buffer "ketchup")
;; (>!! echo-buffer "ketchup") ;; blocks because the chan buffer is full

(def hi-chan (chan))
(doseq [n (range 1000)]
  (go (>! hi-chan (str "hi " n))))

;; thread

(thread (println (<!! echo-chan)))
(>!! echo-chan "mustard")

(defn hot-dog-machine
  []
  (let [in (chan)
        out (chan)]
    (go (<! in)
        (>! out "hot dog"))
    [in out]))

(let [[in out] (hot-dog-machine)]
  (>!! in "pocket lint")
  (<!! out))
;; => "hot dog"

(defn hot-dog-machine-v2
  [hot-dog-count]
  (let [in (chan)
        out (chan)]
    (go (loop [hc hot-dog-count]
          (if (> hc 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do (>! out "hot dog")
                    (recur (dec hc)))
                (do (>! out "wilted lettuce")
                    (recur hc))))
            (do (close! in)
                (close! out)))))
    [in out]))

(let [[in out] (hot-dog-machine-v2 2)]
  (>!! in "pocket lint")
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (<!! out))

(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  (go (>! c2 (clojure.string/upper-case (<! c1))))
  (go (>! c3 (clojure.string/reverse (<! c2))))
  (go (println (<! c3)))
  (>!! c1 "redrum"))

;; Queues

(defn append-to-file
  "Write a string to the end of a file"
  [filename s]
  (spit filename s :append true))

(defn format-quote
  "Delineate the beginning and end of a quote because it's convenient"
  [quote]
  (str "=== BEGIN QUOTE ===\n" quote "=== END QUOTE ===\n\n"))

(defn random-quote
  "Retrieve a random quote and format it"
  []
  (format-quote (slurp "http://www.braveclojure.com/random-quote")))

(defn snag-quotes
  [filename num-quotes]
  (let [c (chan)]
    (go (while true (append-to-file filename (<! c))))
    (dotimes [n num-quotes] (go (>! c (random-quote))))))
