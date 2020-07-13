(ns clojure-noob.ch8
  (:require [clojure.string :as str])
  (:gen-class))

;; Writing Macros

;; Need to quote expressions to get unevaluated data structures back in the final list
;; need to careful about the difference between a symbol and a value

(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

;; quoting produces an unevaluated data structure

(+ 1 2)
;; => 3

'(+ 1 2)
;; => (+ 1 2)

;; the ' character is a reader macro for the quote function

(macroexpand '(when (the-cows-come :home)
                (call me :pappy)
                (slap me :silly)))
;; => (if (the-cows-come :home) (do (call me :pappy) (slap me :silly)))

;; when macro uses if and do special forms internally

(defmacro unless
  "Inverted if"
  [test & branches]
  (conj (reverse branches) test 'if))

(macroexpand '(unless (done-been slapped? me)
                      (slap me :silly)
                      (say "I reckon that'll learn me")))
;; => (if
;;     (done-been slapped? me)
;;     (say "I reckon that'll learn me")
;;     (slap me :silly))


;; Syntax Quoting -> ` includes the symbol's fully namespace

'+
;; => +

`+
;; => clojure.core/+

'(+ 1 2)
;; => (+ 1 2)

`(+ 1 2)
;; => (clojure.core/+ 1 2)

`(+ 1 ~(inc 1)) ;; using ~ inside ` unquotes the form
;; => (clojure.core/+ 1 2)

(list '+ 1 (inc 1))
;; => (+ 1 2)

`(+ 1 ~(inc 1))
;; => (clojure.core/+ 1 2)

;; Syntax quoted version is more concise.

(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  (list 'do
        (list 'println
              "Great squid of Madrid, this is bad code:"
              (list 'quote bad))
        (list 'println
              "Sweet Gorilla of Manilla, this is good code:"
              (list 'quote good))))

;; Re-written using syntax quoting

(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  `(do
     (println "Great squid of Madrid, this is bad code:"
              (quote ~bad))
     (println "Sweet gorilla of Manilla, this is good code:"
              (quote ~good))))

(code-critic [12 3] [4 3 1])

;; macros receive unevaluated, arbitrary data structures as arguments and return data structures that Clojure evaluates.

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic
  [bad good]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code:" bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:" good)))

(defmacro code-critic
  [bad good]
  `(do ~(map #(apply criticize-code %)
             [["Great squid of Madrid, this is bad code:" bad]
              ["Sweet gorilla of Manila, this is good code:" good]])))

(code-critic [12 3] [4 3 1])
;; NullPointerException

;; Need to use unquote splicing.
;; unquote splicing unwraps a seqable data structure, placing its contents directly within the enclosing syntax-quoted data structure

`(+ ~(list 1 2 3))
;; => (clojure.core/+ (1 2 3))

`(+ ~@(list 1 2 3))
;; => (clojure.core/+ 1 2 3)

(defmacro code-critic
  [bad good]
  `(do ~@(map #(apply criticize-code %)
              [["Sweet lion of Zion, this is bad code:" bad]
               ["Great cow of Moscow, this is good code:" good]])))

(code-critic (+ 1 2) (1 + 1))

;; Gotchas

;; Variable Capture - Macro introduces a binding that overwrites an existing binding

;; gensym -> produces a unique symbol that can be bound to variables inside macros
;; used to prevent variable capture within macros

;; auto-gensym -> appending a hashtag to the end of a symbol within a syntax-quoted list

;; Double Evaluation
;; when a form passed to a macro as an argument gets evaluated more than once

(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) " was successful: " ~to-try)
     (println (quote ~to-try) " was not successful: " ~to-try)))

(macroexpand '(report (do (Thread/sleep 1000) (+ 1 1))))
;; => (if
;;     (do (Thread/sleep 1000) (+ 1 1))
;;     (clojure.core/println
;;      '(do (Thread/sleep 1000) (+ 1 1))
;;      " was successful: "
;;      (do (Thread/sleep 1000) (+ 1 1)))
;;     (clojure.core/println
;;      '(do (Thread/sleep 1000) (+ 1 1))
;;      " was not successful: "
;;      (do (Thread/sleep 1000) (+ 1 1))))

;; Validating Functions

(def order-details
  {:name "Mitch Simmons"
   :email "mitch.simgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email address" not-empty

    "Your email address doesn't look like an email"
    #(or (empty %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))
