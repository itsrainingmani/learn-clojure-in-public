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

(eval (list 'def 'lucky-number (concat addition-list [10])))

;; Reader 
;; reader form -> textual representation of data structures

;; text goes to reader -> clojure reads the stream -> produces the data structures -> evaluate the data structure -> prints the textual repr


(read-string "(+ 1 2)") ;; reads the string and produces a data structure
;; => (+ 1 2)

(list? (read-string "(+ 1 2)"))

(conj (read-string "(+ 1 2)") :zigzag)
;; => (:zigzag + 1 2)

(eval (read-string "(+ 1 2)"))
;; => 3

(read-string "#(+ 1 %)")
;; => (fn* [p1__7801#] (+ 1 p1__7801#))

;; the reader uses a reader macro to transform the anon function into a different representation

;; reader macro is a set of rules for transforming text into data structures
;; allow for compact representation of data structures

(read-string "'(a b c)")
;; => (quote (a b c))

;; ', # and @

(read-string "@var")
;; => (clojure.core/deref var)

;; Evaluator -> takes DS, process the DS using rules and returns a result

;; (+ 1 2)
;; Because it’s a list, the evaluator starts by evaluating the first element in the list. The first element is the plus symbol, and the evaluator resolves that by returning the corresponding function. Because the first element in the list is a function, the evaluator evaluates each of the operands. The operands 1 and 2 evaluate to themselves because they’re not lists or symbols. Then the evaluator calls the addition function with 1 and 2 as the operands, and returns the result.

;; Symbols -> use symbols to name functions, macros and other data and evalutates them by resolving them.

;; resolving -> check the local bindings -> look up symbol in the namespace map -> evaluate

;; nested bindings -> most recent one takes precedence

;; (map inc [1 2 3])

;; map and inc both refer to functions. they're not the functions themselves
;; map is a symbol that refers to a function

(type (read-string "map"))
;; => clojure.lang.Symbol

;; on their own symbols and referents don't actually do anything. clojure has to evaluate them to produce a result

;; macros are executed in between the reader and the evaluator -> takes the data structure the reader returns and transforms them into a different data structure that is passed to the evaluator.

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(ignore-last-operand (+ 1 2 10))
;; => 3

(ignore-last-operand (+ 1 2 (println "he")))
;; => 3. nothing was printed

;; when you call a macro, the operands are not evaluated. the symbols are passed along as symbols

;;macroexpansion -> process of determining the return value of a macro
;; data structure returned by a macro is evaluated
;; data structure returned by a function is not

(macroexpand '(ignore-last-operand (+ 1 2 10)))
;; => (+ 1 2)

;; macros -> syntactic abstraction -> use clojure to extend itself

;; The evaluator processes data structures based on their type: symbols are resolved to their referents; lists result in function, macro, or special form calls; and everything else evaluates to itself

;; Chapter exercises
;; 1. Use the list function, quoting, and read-string to create a list that, when evaluated, prints your first name and your favorite sci-fi movie.

(eval (list 'println (read-string "'(\"Mani\" \"Event Horizon\")")))

(read-string "(1 + 2 * 3)")

;; Create an infix function that takes a list like (1 + 3 * 4 - 5) and transforms it into the lists that Clojure needs in order to correctly evaluate the expression using operator precedence rules.
(defn infix
  [math]
  (if (= (count math) 3)
    (list (second math) (first math) (last math))
    (list (second math) (first math) (infix (drop 2 math)))))

(infix (read-string "(1 + 4 * 3 - 2)"))
