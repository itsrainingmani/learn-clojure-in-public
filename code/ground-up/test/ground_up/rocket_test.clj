(ns ground-up.rocket-test
  (:require [clojure.test :refer :all]
            [ground-up.rocket :refer :all]))

(deftest spherical-coordinate-test
  (let [pos {:x 1 :y 2 :z 3}]
    (testing "roundtrip"
      (is (= pos (-> pos cart->sphere sphere->cart))))))

(deftest makes-orbit
  (let [trajectory (->> (atlas-v)
                        prepare
                        (trajectory 1))]
    (when (crashed? trajectory)
      (println "Crashed at" (crash-time trajectory) "seconds")
      (println "Max atltitude" (apoapsis trajectory)
               "meters at" (apoapsis-time trajectory) "seconds"))))
