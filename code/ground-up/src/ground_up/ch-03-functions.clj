(ns ground-up.ch3
  (:gen-class))

(defn -main
  "File that holds code for Ch3"
  [& args]
  (println "Welcome to Clojure from the Ground Up"))

;; Ch3 - Functions

;; Symbols are names for things and when evaluated, Clojure replaces the symbols with their corresponding values.

;; define a meaning for a symbol within a specific expr with let
(let [cats 5] (str "I have " cats " cats."))

;; let applies only within the local expr. It can override any existing defintions for symbols.
(let [+ -] (+ 2 3)) ;; => -1

;; provide multiple bindings within let
(let [person   "joseph"
      num-cats 186]
  (str person " has " num-cats " cats!")) ;; => joseph has 186 cats

;; multiple bindings are evaluated in order. later bindings can use earlier ones
(let [cats 3
      legs (* 4 cats)]
  (str legs " legs all together")) ;; => 12 legs all together

;; Functions

;; (let [x] (+ x 1))
;; This won't compile since x is unbound. This is the nature of a Function.
;; A function is just an expression with unbound symbols

(fn [x] (+ x 1))

;; Almost all verbs in Clojure are functions. They are expressions that have not yet been evaluated. When we invoke a function with a particular value, the expressions are evaluated with the bound value

((fn [x] (+ x 1)) 2) ;; => 3

;; x is the function arg or parameter.
;; (inc 2) -> inc is called with 2, 2 is passed to inc.
;; Function invocation -> Return Value

;; let is evaluated immediately, whereas functions are evaluated later when bindings are provided

(let [burrito #(list "beans" % "cheese")]
  (burrito "carnitas"))

;; We use functions to compact redundant expressions, by isolating common patterns of computation. Symbols help us re-use functions.

;; Vars

(def cats 5)
cats ;; => 5

(type #'ground-up.ch3/cats) ;; clojure.lang.Var

;; def defines a var. Vars, like symbols are references to other values. When evaluated, the symbol pointing to the var is replaced by the var's correspoding value. useful to think of symbols and vars as existing in some kind of global hash-map and they are indexed by their name.

;; The symbol 'inc points to the var #'clojure.core/inc which in turn points to the function #<core$inc clojure.core$inc@16bc0b3c>

(def astronauts []) ;; => #'ground-up.core/astronauts
(count astronauts) ;; => 0

(def astronauts ["Sally Ride" "Guy Bluford"])
(count astronauts) ;; => 2

;; The value of astronauts depends on when we evaluated it. Redefining names this way changes the meaning of expressions everywhere in a program.
;; Good for REPL work. But could be dangerous in production
;; Idiomatic way is to use def to set up a program initially and only change those defs with careful though

;; Defining functions

(def half (fn [number] (/ number 2)))
;; use def to map the half symbol to the anonymous function

;; There is a short hand for this - defn
(defn half [number] (/ number 2))
(half 24) ;; => 12

;; arity -> number of arguments to a function
;; Functions can be variadic or unary

(defn half
  ([] 1/2)
  ([x] (/ x 2)))
(half)    ;; => 1/2
(half 10) ;; => 10

;; Functions can take n args, this can be represented with &. & slurps up all remaining args as a list

(defn vargs
  [x y & more-args] ;; x and y are mandatory
  {:x    x
   :y    y
   :more more-args})
(vargs 1 2 3 4) ;; => {:x 1, :y 2, :more (3 4)}

;; Docstrings
(defn launch
  "Launches a spacecraft into the given orbit by initiaiting a
  controller on-axis burn. Does not automatically stage, but
  does vector thrust, if the craft supports it."
  [craft target-orbit]
  "OK, we don't know how to control the space craft yet")

;; docstring is shown at (doc launch)
