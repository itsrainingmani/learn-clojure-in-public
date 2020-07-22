(ns clojure-noob.ch13
  (:require [clojure.repl :refer :all])
  (:gen-class))

;; Multimethods

(defmulti full-moon-behavior (fn [were-creature] (:were-type were-creature)))

;;         method-name        dispatch-value
(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) " will howl and murder"))

;;         method-name        dispatch-value
(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) " will encourage people and sweat to the oldies"))

(full-moon-behavior {:were-type :wolf
                     :name "Rachel from next door"})
;; => "Rachel from next door will howl and murder"

(full-moon-behavior {:name "Andy the baker"
                     :were-type :simmons})
;; => "Andy the baker will encourage people and sweat to the oldies"

;; default method
(defmethod full-moon-behavior :default
  [were-creature]
  (str (:name were-creature) " will stay up all night fantasy footballing"))

(full-moon-behavior {:name "Jim from Sales"
                     :were-type :office-worker})
;; => "Jim from Sales will stay up all night fantasy footballing"

(defmulti types (fn [x y] [(class x) (class y)]))
(defmethod types [java.lang.String java.lang.String]
  [x y]
  "Two Strings!")

(types "String 1" "String 2")
;; => "Two Strings!"

(defprotocol Psychodynamics
  "Plumb the inner depthds of your data types" ;; docstring
  (thoughts [x] "The data type's innermost thoughts") ;; method signature
  (feelings-about [x] [x y] "Feelings about self or other")) ;; method signture

;; method signature - name, argument specification and optional docstring

(extend-type java.lang.String
  Psychodynamics
  (thoughts [x] (str x " thinks, 'Truly the character defines the data type"))
  (feelings-about
    ([x] (str x " is longing for a simpler way of life"))
    ([x y] (str x " is envious of " y "'s simpler way of life"))))

(thoughts "blorb")
;; => "blorb thinks, 'Truly the character defines the data type"

(feelings-about "schmorb")
;; => "schmorb is longing for a simpler way of life"

(feelings-about "schmorb" 2)
;; => "schmorb is envious of 2's simpler way of life"

(extend-type java.lang.Object
  Psychodynamics
  (thoughts [x] "Maybe the Internet is just a vector for toxoplasmosis")
  (feelings-about
    ([x] "meh")
    ([x y] (str "meh about " y))))

(thoughts 3)
;; => "Maybe the Internet is just a vector for toxoplasmosis"

(feelings-about 3)
;; => "meh"

;;         name     fields
(defrecord WereWolf [name title])
;; records are actually java classes

;; class instantiation interop call
(WereWolf. "David" "London Tourist")
;; => {:name "David", :title "London Tourist"}

;; -> and map-> are factory functions for records
(->WereWolf "Jacob" "Lead Shirt Discarder")
;; => {:name "Jacob", :title "Lead Shirt Discarder"}

(map->WereWolf {:name "Lucian" :title "CEO of YouTube"})
;; => {:name "Lucian", :title "CEO of YouTube"}

(defprotocol WereCreature
  (full-moon-behavior [x]))

(defrecord WereWolf [name title]
  WereCreature
  (full-moon-behavior [x]
    (str name " will howl and murder")))

(full-moon-behavior (map->WereWolf {:name "PewDie" :title "Chairman of YT"}));; => "PewDie will howl and murder"

;; Chapter Exercises
;; 1. Extend the full-moon-behavior multimethod to add behavior for your own kind of were-creature.

(defmethod full-moon-behavior :paul-bro
  [were-creature]
  (str "Here lies " (:name were-creature) " Paul."))

(full-moon-behavior {:name "Jake" :were-type :paul-bro})
;; => "Here lies Jake Paul."

;; 2. Create a WereSimmons record type, and then extend the WereCreature protocol.

(defrecord WereSimmons [name title]
  WereCreature
  (full-moon-behavior [x]
    (str name " is the " title)))

(full-moon-behavior (map->WereSimmons {:name "Damien" :title "Manager of IKEA"}))
;; => "Damien is the Manager of IKEA"

;; 3. Create your own protocol, and then extend it using extend-type and extend-protocol.

(defprotocol WereWolves
  "Aware Wolf"
  (aware-wolf [x] "An enlightened wolf")
  (wear-wolf [x] "Feelings about self or other"))

(extend-protocol WereWolves
  java.lang.String
  (aware-wolf [x] (str "This Wolf is extremely Aware of " x))
  (wear-wolf [x] (str "This Wolf is wearing " x)))

(aware-wolf "TikTok")
;; => "This Wolf is extremely Aware of TikTok"

(wear-wolf "shoes")
;; => "This Wolf is wearing shoes"
