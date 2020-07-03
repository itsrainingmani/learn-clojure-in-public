(ns clojure-noob.core
  (:require [clojure.string :as s])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (println "Hello, World!"))

; Boolean Ops
; and returns the first falsey value or the last truthy value if no vals are falsey
; or returns either the first truthy value or the last value

(or false nil :large_I_mean_venti)
(or (= 0 1) (= "yes" "no"))

; Data Structures are immutable
; 
; Strings - no interpolation. only concatenation
; 
;; (def severity :mild)
;; (def error-message "OH GOD! IT'S A DISASTER! WE'RE ")
;; (if (= severity :mild)
;;   (def error-message (str error-message "MILDLY INCONVENIENCED!"))
;;   (def error-message (str error-message "DOOOOOOOMED!")))
;;   
;;   This is bad because we're changing the value of error-message as we progress
;;   This could lead to unexpected side effects
;;   

(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED"
         "DOOOOOOMED!")))

; Maps - way of associating some value to another
; hash-map to create a map
; 
(hash-map :a 1 :b 2)
;; => {:b 2, :a 1}

(get {:a 0 :b 1} :c)
;; => nil

(get {:a 0 :b 2} :c "Default value")
;; => "Default value"

;; Nested map lookup
(get-in {:a 0 :b {:c "hum"}} [:b :c])
;; => "hum"

(:a {:a 1 :b 2 :c 3})
;; => 1
;; Use keywords as functions to look up the corresponding value in a data structure

; Vector -> 0 indexed heterogenous collection [1 2 3]
(get [1 2 3] 0)
;; => 1
(conj [1 2 3] 4) ;; End of the vector
;; => [1 2 3 4]


; List -> linked collection of values. Heterogenous
(nth '(1 2 3) 2)
;; => 3
(conj '(1 2 3) 4) ;; Beginning of the list
;; => (4 1 2 3)

; Set -> collection of unique values (heterogenous)
(hash-set 1 2 1 2)
;; => #{1 2}
(conj #{:a :b} :b)
;; => #{:b :a}
(set [2 2 3 4 4])
;; => #{4 3 2}
;; Membership with contains?, get or keyword as function
(contains? #{:a :b} :a)
;; => true
(get #{:a :b} :a)
;; => :a
(get #{:a :b} "kurt vonnegut")
;; => nil

;; Functions
((first [+ 0]) 1 2 3)
;; => 6
;; (first [+ 0]) returns the + function
;; Valid function calls need to have a function as the first item in the list
;; 
;; Functions that take other functions as args or return functions are called
;; Higher-Order Functions
;; 
;; Clojure lets you create functions that generalize over processes rather than data
;; 
;; Clojure evaluates all function args recursively before passing them to the function
(+ (inc 199) (/ 100 (- 7 2)))

;; The args are recursively evaluated from the deepest level first
(+ 200 (/ 100 5))
(+ 200 20)
;; => 220

;; Special forms -> if, def, defn etc. Core Functionality that can't be implemented with other functions
;; Can't be used as args to functions
;; 
;; Function definitions - can have more arguments of different lengths
;; (defn multi-arity
;;   ;; 3-arity arguments and body
;;   ([first-arg second-arg third-arg]
;;    (do-things first-arg second-arg third-arg))
;;   ;; 2-arity arguments and body
;;   ([first-arg second-arg]
;;    (do-things first-arg second-arg))
;;   ;; 1-arity arguments and body
;;   ([first-arg]
;;    (do-things first-arg)))

;; This example uses arity overloading

;; Destructuring
;; Lets you concisely bind names to values within a collection
;; 
(defn my-first
  [[first-thing]]
  first-thing)

(my-first ["my axe" "my sword" "my shield"])
;; => "my axe"

;; Using a vector within the arg list tells Clojure that the function is receiving a list or vector as an argument and you want the contents of the list to be taken apart and meaningfully associate symbols to it
;; 
(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choices. "
                "Here they are in case you need to cry over them: "
                (s/join ", " unimportant-choices))))

(chooser ["Marmalade", "Handsome Jack", "Pigpen", "Aquaman"])

;; Destructuring maps
(defn announce-treasure-location
  [{lat :lat lng :lng}] ;; [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

(announce-treasure-location {:lat 28.22 :lng 81.33})

;; Destructuring -> Telling clojure to associate names with values in a list, map, set or vector.

;; Function Body can contain any number of expressions. It returns the last one
;; Clojure treats all functions the same
;; (fn [param-list] function-body) is equivalent to

(#(identity %&) 1 "blarg" :yip)
;; => (1 "blarg" :yip)

;; Closures
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))
;; => #'clojure-noob.core/inc-maker


(def inc3 (inc-maker 3))
;; => #'clojure-noob.core/inc3

(inc3 7)
;; => 10
