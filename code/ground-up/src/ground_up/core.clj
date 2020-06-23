(ns ground-up.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Welcome to Clojure from the Ground Up"))


;; Ch 2
;; Types
;; Since Clojure is built on top of Java, many of its types are plain old Java Types

;; Integers

(type 3) ;; java.lang.Long
Long/MAX_VALUE

;; (inc Long/MAX_VALUE) overflows with an ArithmeticException integer overflow

(inc (bigint Long/MAX_VALUE)) ;; This coerces Long/MAX_VALUE into a BigInt and then increments it. BigInts can have arbitrary-precision and are prefixed with an N at the end

(type (int 0))   ;; java.lang.Integer
(type (short 0)) ;; java.lang.Short
(type (byte 0))  ;; java.lang.Byte

;; Integers are half the size of Longs; they store values in 32 bits. Shorts are 16 bits, and Bytes are 8.

;; To represent numbers between integers, we often use floating-point numbers, which can represent small numbers with fine precision, and large numbers with coarse precision

(type 1.23)         ;; java.lang.Double
(type (float 1.23)) ;; java.lang.Float
(type 1/3)          ;; clojure.lang.Ratio
