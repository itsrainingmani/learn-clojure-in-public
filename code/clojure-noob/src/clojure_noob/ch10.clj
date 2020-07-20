(ns clojure-noob.ch10
  (:require [clojure.repl :refer :all])
  (:gen-class))

(defn- ch10 [] (println "Welcome to Ch10"))
(ch10)

(def fred (atom {:cuddle-hunger-level 0
                 :percent-deteriorated 60}))


(println @fred) ;; doesn't block

(let [zombie-state @fred]
  (if (>= (:percent-deteriorated zombie-state) 50)
    (future (println (:cuddle-hunger-level zombie-state)))
    (future (println ))))

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level 1})))
;; => {:cuddle-hunger-level 5, :percent-deteriorated 60}

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level 1
                                      :percent-deteriorated 1})))

;; function can take multiple args

(defn increase-cuddle-hunger-level
  [zombie-state increase-by]
  (merge-with + zombie-state {:cuddle-hunger-level increase-by}))

(increase-cuddle-hunger-level @fred 10)
;; => {:cuddle-hunger-level 16, :percent-deteriorated 61}

(swap! fred increase-cuddle-hunger-level 10)
;; => {:cuddle-hunger-level 26, :percent-deteriorated 61}

@fred
;; => {:cuddle-hunger-level 26, :percent-deteriorated 61}

(update-in {:a {:b 3}} [:a :b] inc)
;; => {:a {:b 4}}

(update-in {:a {:b 3}} [:a :b] + 10)
;; => {:a {:b 13}}

(swap! fred update-in [:cuddle-hunger-level] + 10)
;; => {:cuddle-hunger-level 22, :percent-deteriorated 61}

;; You can use atoms to retain past state

(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1:" s1)
  (println "Current state:" @num))

;; update an atom without checking it's current value
(reset! fred {:cuddle-hunger-level 0
              :percent-deteriorated 0})
;; => {:cuddle-hunger-level 0, :percent-deteriorated 0}

;; watch

(defn shuffle-speed
  [zombie]
  (* (:cuddle-hunger-level zombie)
     (- 100 (:percent-deteriorated zombie))))

;; alert when a zombie's shuffle speed reaches 5000 SPH

(defn shuffle-alert
  [key watched old-state new-state]
  (let [sph (shuffle-speed new-state)]
    (if (> sph 5000)
      (do
        (println "Run, you fool!")
        (println "The zombie's SPH is now " sph)
        (println "This message brought to your courtesy of " key))
      (do
        (println "All's well with " key)
        (println "Cuddle hunger: " (:cuddle-hunger-level new-state))
        (println "Percent deteriorated: " (:percent-deteriorated new-state))
        (println "SPH: " sph)))))

;; watch functions take 4 args - a key for reporting, atom being watches, state of the atom before the update and the state of the atom after the update

(reset! fred {:cuddle-hunger-level 22
              :percent-deteriorated 2})

;; add a watcher - takes in a ref, key and the watcher function
(add-watch fred :fred-shuffle-alert shuffle-alert)

(swap! fred update-in [:percent-deteriorated] + 1)

(swap! fred update-in [:cuddle-hunger-level] + 30)

;; Validator - what states are allowable for a reference

;; ensure a zombie's percent-deteriorated is between 0 and 100
(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0)
       (<= percent-deteriorated 100)))

;; add validator during atom creation
(def bobby
  (atom
   {:cuddle-hunger-level 0 :percent-deteriorated 0}
   :validator percent-deteriorated-validator))

(swap! bobby update-in [:percent-deteriorated] + 101)
;; throws an exception

;; Refs

(def sock-varieties
  #{"darned" "argyle" "wool" "horsehair" "mulleted"
    "passive-aggressive" "striped" "polka-dotted"
    "athletic" "business" "power" "invisible" "gollumed"})

(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count count})

(defn generate-sock-gnome
  "Create an initial sock gnome state with no socks"
  [name]
  {:name name
   :socks #{}})

(def sock-gnome (ref (generate-sock-gnome "Barumpharumph")))
(def dryer (ref {:name "LG 1337"
                 :socks (set (map #(sock-count % 2) sock-varieties))}))

(:socks @dryer)
;; => #{{:variety "gollumed", :count 2} {:variety "striped", :count 2}
;;      {:variety "wool", :count 2}
;;      {:variety "passive-aggressive", :count 2}
;;      {:variety "argyle", :count 2} {:variety "business", :count 2}
;;      {:variety "darned", :count 2} {:variety "polka-dotted", :count 2}
;;      {:variety "horsehair", :count 2} {:variety "power", :count 2}
;;      {:variety "athletic", :count 2} {:variety "mulleted", :count 2}
;;      {:variety "invisible", :count 2}}

(defn steal-sock
  [gnome dryer]
  (dosync
   (when-let [pair (some #(if (= (:count %) 2) %) (:socks @dryer))]
     (let [updated-count (sock-count (:variety pair) 1)]
       (alter gnome update-in [:socks] conj updated-count)
       (alter dryer update-in [:socks] disj pair)
       (alter dryer update-in [:socks] conj updated-count)))))

(steal-sock sock-gnome dryer)

(:socks @dryer)
;; => #{{:variety "striped", :count 2} {:variety "wool", :count 2}
;;      {:variety "passive-aggressive", :count 2}
;;      {:variety "argyle", :count 2} {:variety "business", :count 2}
;;      {:variety "darned", :count 2} {:variety "polka-dotted", :count 2}
;;      {:variety "horsehair", :count 2} {:variety "power", :count 2}
;;      {:variety "athletic", :count 2} {:variety "gollumed", :count 1}
;;      {:variety "mulleted", :count 2} {:variety "invisible", :count 2}}

(:socks @sock-gnome)
;; => #{{:variety "gollumed", :count 1}}

(def counter (ref 0))
(future
  (dosync
   (alter counter inc)
   (println @counter)
   (Thread/sleep 500)
   (alter counter inc)
   (println @counter)))
(Thread/sleep 250)
(println @counter)

;; commute

(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))
(def counter (ref 0))
(future (dosync (commute counter (sleep-print-update 100 "Thread A" inc))))
(future (dosync (commute counter (sleep-print-update 150 "Thread B" inc))))

@counter
;; => 2

;; var

;; associations between symbols and objects

(def ^:dynamic *notification-address* "dobby@elf.org")

;; temporarily change the value of a dynamic var
(binding [*notification-address* "test@elf.org"]
  *notification-address*)
;; => "test@elf.org"

*notification-address*
;; => "dobby@elf.org"

(def ^:dynamic *troll-thought* nil)
(defn troll-riddle
  [your-answer]
  (let [number "man meat"]
    (when (thread-bound? #'*troll-thought*) ;; checks if the var has been bound
      (set! *troll-thought* number))
    (if (= number your-answer)
      "TROLL: You can cross the bridge!"
      "TROLL: Time to eat you, succulent human!")))

(binding [*troll-thought* nil]
  (println (troll-riddle 2))
  (println "Succulent Human: OOOH! The answer was " *troll-thought*))

*troll-thought*

;; var root
(def power-source "hair")

(alter-var-root #'power-source (fn [_] "7 eleven parking lot"))
power-source
;; => "7 eleven parking lot"

(with-redefs [*out* *out*]
  (doto (Thread. #(println "with redefs allows me to show up in REPL"))
    .start
    .join))

;; pmap

(defn always-1
  []
  1)

(take 5 (repeatedly always-1))
;; => (1 1 1 1 1)

(take 5 (repeatedly (partial rand-int 10)))
;; => (6 6 8 7 9)

(def alphabet-length 26)

(def letters (mapv (comp str char (partial + 65)) (range alphabet-length)))

(defn random-string
  "Returns a random string of specified length"
  [length]
  (apply str (take length (repeatedly #(rand-nth letters)))))

(defn random-string-list
  [list-length string-length]
  (doall (take list-length (repeatedly (partial random-string string-length)))))

(def orc-names (random-string-list 3000 7000))

(time (dorun (map clojure.string/lower-case orc-names)))
;; "Elapsed time: 295.426771 msecs"

(time (dorun (pmap clojure.string/lower-case orc-names)))
;; "Elapsed time: 129.370606 msecs"
