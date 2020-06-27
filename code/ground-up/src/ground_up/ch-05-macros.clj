(ns ground-up.ch5
  (:use clojure.repl)
  (:gen-class))

(defn -main
  [& args]
  (println "Welcome to Chapter 5 of Clojure from the Ground Up"))

;; Control flow

(if (= 2 2) :a :b)

(if (pos? -5)
  (prn "-5 is positive")
  (do
    (prn "-5 is negative")
    (prn "Who'd have thought")))

;; use do to evaluate more than one expression in order
;; useful when the expressions have side effects

(when false
  (prn :hi)
  (prn :there))

(when true
  (prn :hi)
  (prn :there))

;; when takes any number of expressions and only evaluates them when the predicate is true. nil otherwise
;; opposite of when is when-not.

;; when-let performs an operation and if it's truthy, binds it to a var that can be re-used in the body of when-let
(when-let [x (+ 1 2 3 4)]
  (str x)) ;; "10"

(when-let [x (first [])]
  (str x)) ;; => nil

;; There's also if-let

;; while evalutes expr as long as the pred is truthy. useful for side effects.
(def x 0)
(while (< x 5) (prn x) (def x (inc x))) ;; here x is bound to 0. In the body of while, x is rebound to an incremented value

(cond
  (= 2 5) :nope
  (= 3 3) :yep
  (= 5 5) :cant-get-here
  :else   :a-default-value) ;; => :yep

;; cond is basically a condensed if-elseif-eslse stmt but without evaluating a variable
;; to do that use condp

;; condp evaluates each branch with the same predicate and variable and outputs the first value for the expression that evals to true
(defn age-groups
  [age]
  (condp <= age
    40 :boomer
    20 :millenial
    10 :kindergarten
    :baby))

;; case is an expression matching construct. It jumps to the expression that matches. (doesn't evaluate the tests in order)

(defn with-tax
  "computes the total cost with tax of a purchase in the given state"
  [state subtotal]
  (case state
    :WA (* 1.065 subtotal)
    :OR subtotal
    :CA (* 1.075 subtotal)
    subtotal))

(defn sum [numbers]
  (if-let [n (first numbers)]
    (+ n (sum (rest numbers)))
    0))

;; This function uses recursion to compute the sum but it has a flaw. When the function calls itself deeply over and over again.
;; Evertime the function is called (during recursion) the args for the function are stored on the stack. We can't re-use the memory
;; until every single recursive calls have finished. During the process, we can run out of stack frames to allocate.

(defn sum
  ([numbers]
   (sum 0 numbers))
  ([subtotal numbers]
   (if-let [n (first numbers)]
     (recur (+ subtotal n) (rest numbers))
     subtotal)))

;; recur takes the place of sum. The final value doesn't depend on the results of the recursion anymore since recur hints to the compiler
;; that we don't need to hold on the stack. This allows for a much more efficient memory usage.

;; Laziness
;; lazy-seq macro provides lazy evaluation.

(defn integers
  [x]
  (lazy-seq
   (cons x (integers (inc x)))))

;; sequence doesn't terminate but it's infinitely recursive.
;; lazy-seq interrupts the recursion and restructures it into a sequences which constructs the elements only when requested

;; delay -> Takes a body of expressions and yields a delay object.
;; The delay object will invoke the body only the first time when it;s forced with force or deref. caches the result
;; for subsequent calls

(def x (delay (prn "computing a big number") (last (take 10000000 (iterate inc 0))))) ;; => returns a Delay object
;; realized? is false

#_ (deref x)
;; this invokes body of x for first time. realized? is now true

;; List Comprehensions

;; for is the list comp macro. Kind of works like map when it's simple.
;; Takes a vector of bindings (like let). Unlike let, for binds it's vars
;; to each possible combination of elements in their corresponding sequences.

;; for yields lazy sequences. These can be chained with other sequence operators like take, filter.
;; Use :while to tell for when to stop
;; :when to filter out combinations of elements
