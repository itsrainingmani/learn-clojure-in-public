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

;; Lisps evaluate native data structures
;; Clojure still evaluates tree structures, but the trees are structured using Clojure liss and the nodes are Clojure values

;; Clojure reader converts code (+ 1 (* 6 7)) into a nested list.
;; Clojure evaluator takes that data as input and produces a result. (also compiles to the JVM bytecode)

(def addition-list (list + 1 2))
(eval addition-list)
;; => 3

;; Send program's data structures directly to the clojure evaluator with 'eval'

;; Reader 
