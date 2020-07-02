(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (println "Hello, World!"))

(defn testcalva [x] (* x 2))

(defn new-map
  "Re-implementing the map function"
  [f xs]
  (reduce (fn [acc x] (conj acc (f x))) [] xs))