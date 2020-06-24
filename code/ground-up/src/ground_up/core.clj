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

(type #'ground-up.core/cats) ;; clojure.lang.Var

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
