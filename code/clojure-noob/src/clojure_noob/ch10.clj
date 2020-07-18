(ns clojure-noob.ch10
  (:require [clojure.repl :refer :all])
  (:gen-class))

(defn- ch10 [] (println "Welcome to Ch10"))
(ch10)

(def fred (atom {:cuddle-hunger-level 0
                 :percent-deteriorated 0}))


(println @fred) ;; doesn't block
