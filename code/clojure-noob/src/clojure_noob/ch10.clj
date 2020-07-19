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
