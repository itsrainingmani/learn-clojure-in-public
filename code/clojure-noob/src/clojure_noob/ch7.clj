(ns clojure-noob.ch7
  (:gen-class))

;; Macros allow you to transform arbitrary expressions into valid Clojure

(defmacro backwards
  [form]
  (reverse form))

(backwards (" backwards " " am" "I" str))
;; => "I am backwards "

;; Evaluation model
;; Reads textual source code first -> produces Clojure data structures
;; Clojure data structures -> evaluated

;; Homoiconic -> reason about the code as a set of data structures that you can manipulate programmatically
