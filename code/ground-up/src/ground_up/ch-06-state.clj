(ns ground-up.ch6
  (:gen-class))

(defn -main
  [& xs]
  (println "Welcome to Chapter 6 of Clojure from the Groun Up"))


(defn present
  [gift] ;; The value of gift pased by the outer function is remembered by the inner one
  (fn [] gift))

(do (prn "Adding") (+ 1 2))

(def later (fn [] (do (prn "Adding") (+ 1 2))))
(later)

;; Evaluating def later .. did not evaluate the expressions in the function body.
;; The function body defers evaluation of expressions.

(def later (delay (prn "Adding") (+ 1 2)))
later ;; => #delay[{:status :pending :val :nil}]

;; delay creates a Delay object; an identity which refers to expressions which should be evaluated later.

(deref later)

;; We can dereference the value of the identity with deref

;; Why use Delay over a normal function? A normal function, while deferring evaluation is not stateful. It would execute the body evertime on invocation. Delays only evaluate their expressions once. They remember their value and return it on successive derefs

@later ;; same as (deref later)

;; Future : Delay that is evaluated in parallel. Futures return immediately, gives us an identity pointing to the value of the last expression in the body.

(def x (future (prn "hi") (+ 1 2))) ;; immediately prints "hi" since it's evaluated in a new thread.

@x ;; returns 3

(dotimes [i 5] (future (prn i)))

;; Would print numbers between 1 and 5 but in wild orders since there are 5 threads running at once.

;; Like Delays, we can deref a future many times but the last expr is only evaluated once


;; Promises
;; Delays defer evaluation. Futures parallelize it.

(def box (promise)) ;; box is pending a value. Derefing box will block the thread since it's waiting for a value.

;; We need to deliver a value to the box
(deliver box :live-scorpions)

@box ;; :live-scorpions

;; However once a promise has been delivered it can't be changed.
;; Once delivered, it always refers to the same value. Promise is also a concurrency primitive. It guarantees that any attempt to read the vaue will wait until the value has been written.

;; Use concurrency primitives to sync a program that is being evaluated concurrently

(def card (promise))

(def dealer (future
              (Thread/sleep 5000)
              (deliver card [(inc (rand-int 13))
                             (rand-nth [:clubs :spades :hearts :diamonds])])))
(deref card)

;; Execute the previous 3 expressions one after the other quickly.
;; card is a promise. We set up a dealer thread that sleeps for 5 seconds and then delivers a random card. While dealer is sleeping, we tried to deref the card. SO the program has to wait until the dealer thread waks and delivers the value.

;; Vars
;; They're transparent mutable references. Each var has a value associated with it and that value can change over time. When a var is eval'd, it is replaced by it's present value transparently - everywhere in the program.


(def x :mouse)
(def box (fn [] x))
(box) ;; :mouse
(def x :cat)
(box) ;; :cat

;; Even though box closed over x, calling box returned different results dependsing on the current value of x

(defn decouple [glider]
  (prn "bolts released"))

(defn launch [glider]
  (decouple glider)
  (prn glider "away!"))

(launch "albatross")

(defn decouple [glider]
  (prn "tether released"))

(launch "albatross")

;; A reference that's the same everywhere is a global var
;; But vars can overriden only within the scope of a particular function call and nowhere else


(def ^:dynamic *board* :maple) ;; Common convention to name dynamic vars with * around them.

(defn cut [] (do (prn "Sawing through " *board*) (elongate-board *board*)))

(cut) ;; => Sawing throuhg :maple

;; Now we can use the binding macro to create a dynamic scope. It creates new bindings for an already created var, executes the given expression, then binds the var back to the previous value.

(binding [*board* :cedar] (cut)) ;; => Sawing through :cedar
(cut) ;; => Sawing through :cedar


;; Dynamic scope also propagates through function calls. So if we defined another function -
(defn elongate-board [brd] (do (prn "Elongating " brd) (list brd brd)))

;; and changed cut to this -

(defn cut [] (do (prn "Sawing through " *board*) (elongate-board *board*)))

;; doing this -
(binding [*board* :cedar] (cut))

;; would affect what happens in elongate-board as well

;; Outside the binding expression, the value is still :maple. This safety should even hold if the program is executed in multiple threads


;; Atoms

;; Vars can be read, set and dynamically bound. However, this read-modify-update process is not threadsafe. It assumes that operations are consecutive. Not concurrent.

;; Enter Atoms - An Identity that supports safe transformations from one state to another.

(def xs (atom #{}))

;; Atoms are not transparent like Vars. You need to deref them to get the underlying value

;; You can safely set an atoms value with reset! (! - the function modifies the state of it's args).

(reset! xs :blah)

;; You can safely update the value of the atom using swap!. swap! uses a pure function, takes the current val of the atom and returns a new val,

;; swap! is linearizable (updates happen in consecutive order)

(def xs (atom #{}))

(dotimes [i 10] (future (swap! xs conj i)))

@xs ;; => #{0 7 1 4 6 3 2 9 5 8}

;; The atom set is updated without any of the updates slipping through.

;; Refs

;; Needed for multi-identity updates. We need a stronger safety guarantee than single-atom linearizability. we want serializability.

(def x (ref 0))

x

@x

(def y (ref 0))

(dosync
(ref-set x 1)
(ref-set y 2))

;; Atoms are updated individually with swap!, but refs are updated in groups with (dosync). ref-set is the ref version of reset!
;; ref equiv of swap! is alter

