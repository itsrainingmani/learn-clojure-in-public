(ns ground-up.ch2
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

;; Mathematical Operations
;; Clojure aims to preserve information. Adding two longs returns a long; adding a double and a long returns a double.

;; Most math functions are multi-arity functions and can be applied from 1 to N inputs
;; eg: +, *, -, /, <, >, <=, >=

;; Strings
(type "cat") ;; java.lang.String

;; Almost anything can be made into a string
(str "cat") ;; "cat"
(str 'cat)  ;; "cat"
(str true)  ;; "true"
(str nil)   ;; ""

;; We can also use the str function to concatenate mutliple values or strings into a single one

(str "Hello, " "There " "General " 3 " Meowbi")

;; Regex
;; A regex is represented by #"...". We can use the re-find to check if the regex occurs in a string and re-matches to give us a vector of matches
(re-find #"cat" "mystic cat house")
(re-matches #"(.+):(.+)" "mouse:treat") ;; ["mouse:treat" "mouse" "treat"]

(defn ct [coll] (reduce + (map #(if % 1 0) coll)))

(#(+ (* 3 4)))

(defn ct1 [coll]
  (->> coll
       (map #(if % 1 0))
       (reduce +)))
