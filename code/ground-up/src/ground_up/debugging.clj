(ns ground-up.debugging
  (:gen-class))

(defn bake
  "Bakes a case for a certain amt of time. Returns a cake with new :tastiness level"
  [pie temp time]
  (assoc pie :tastiness
         (condp (* temp time) <
           400 :burned
           350 :perfect
           300 :soggy)))

;; This is broken. Due to a ClassCastException

(defn bake-fixed
  [pie temp time]
  (assoc pie :tastiness
         (condp < (* temp time)
           400 :burned
           350 :perfect
           300 :soggy)))
