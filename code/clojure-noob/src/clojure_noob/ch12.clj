(ns clojure-noob.ch12
  (:require [clojure.repl :refer :all])
  (:gen-class))

(.toUpperCase "By Bluebeard's bananas!")
;; => "BY BLUEBEARD'S BANANAS!"

(.indexOf "let's synergize our bleeding edges" "y")
;; => 7

;; Equivalent to 
;; "By Bluebeard's bananas!".toUpperCase()
;; "Let's synergize our bleeding edges".indexOf("y")

;; Static method on class
(java.lang.Math/abs -3)
;; => 3

(new String)

(String.)

(String. "To Davey Jone's Locker")
;; => "To Davey Jone's Locker"

(java.util.Stack.)

(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of GoT")
  stack)
;; => ["Latest episode of GoT"]

;; You can use seq functions to read data from a stack
;; But can't use conj or into

(doto (java.util.Stack.)
  (.push "Latest ep of GoT")
  (.push "Whoops, I meant something else"))

;; doto returns the object rather than the value of the method calls

(System/getenv)

(System/getProperty "user.dir")
;; => "/Users/mani/repos/learn-clojure-in-public/code/clojure-noob"

(System/getProperty "java.version")
;; => "14.0.1"

;; IO

(spit "/tmp/hercules-todo-list"
      "- kill dat lion brov
- chop up what nasty multi-headed snake thing")

(slurp "/tmp/hercules-todo-list")
;; => "- kill dat lion brov\n- chop up what nasty multi-headed snake thing"

(with-open [todo-list-str (clojure.java.io/reader "/tmp/hercules-todo-list")]
  (println (first (line-seq todo-list-str))))
;; => kill dat lion bruv
