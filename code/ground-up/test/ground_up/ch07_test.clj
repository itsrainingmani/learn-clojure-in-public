(ns ground-up.ch07-test
  (:require [clojure.test :refer :all]
            [ground-up.ch-07-logistics :refer :all]))

(deftest pow-test
  (testing "unity"
    (is (= 1 (pow 1 1))))

  (testing "squaring integers"
    (is (= 9 (pow 3 2))))
  (testing "0^0"
    (is (= 1 (pow 0 0)))))
