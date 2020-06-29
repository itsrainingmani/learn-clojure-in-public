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

x ;; =>

@x ;; => #ref[{:status :ready :val 0}]

(def y (ref 0))

(dosync
 (ref-set x 1)   ;; x is set to 1
 (ref-set y 2))  ;; y is set to 2

[@x @y] 
;; => [1 2]

;; Atoms are updated individually with swap!, but refs are updated in groups with (dosync). ref-set is the ref version of reset!
;; ref equiv of swap! is alter

(dosync
 (alter x + 2)
 (alter y inc))

[@x @y]

;; Alter operations are performed atomically. Effects are never interleaved with other operations

;; If it's ok for the updates to take place out of order - use commute
;; commute -> commutative. (if transactions are equivalent we don't need very strict ordering constraints)

(dosync
 (commute x + 2)
 (commute y inc))

[@x @y]

(dosync
 (alter x + (ensure y)))
[@x @y]

;; here we used ensure instead of deref to perform a strongly consistent read. It is guaranteed to take place in the same logical order as the dosync transaction.

;; refs are an order of magnitude slower to update than atoms.
;; Only needed when we want to update multiple pieces of state independently. (and where we need to work with distinct but partly overlapping pieces of state). If there's no overlap, better to use atoms.

;; Exercises
;; 1. Use delay to compute this sum lazily. Show it takes no time to return the delay, but roughly 1 second to deref

;; Non-lazy sum
(defn sum [start end] (reduce + (range start end)))
(time (sum 0 1e7))

;; user> (time (sum 0 1e7))
;; "Elapsed time: 909.802143 msecs"
;; 49999995000000

;; Lazy eval using delay
(def lazysum (delay (sum 0 1e7)))

;; user> (time lazysum)
;; "Elapsed time: 0.016645 msecs"
;; #<Delay@5764fdf1: :not-delivered>

;; user> (time @lazysum)
;; "Elapsed time: 881.947225 msecs"
;; 49999995000000

;; 2. We can do the computation in a new thread directly, using (.start (Thread. (fn [] (sum 0 1e7)))–but this simply runs the (sum) function and discards the results. Use a promise to hand the result back out of the thread. Use this technique to write your own version of the future macro.

(def computedsum (promise))

(.start (Thread. (fn [] (deliver computedsum (sum 0 1e7)))))
@computedsum

;; How do we make a macro from this

(defmacro new-future
  [f & args]
  `(let [p# (promise)]
     (do 
       (.start (Thread. (fn [] (deliver p# (~f ~@args)))))
       )
     (fn [] (deref p#))))

(clojure.pprint/pprint (macroexpand '(new-future inc 1)))
(def ss (new-future inc 1))

(def computedsum (new-future sum 0 1e7))
(computedsum)


;; 3. If your computer has two cores, you can do this expensive computation twice as fast by splitting it into two parts: (sum 0 (/ 1e7 2)), and (sum (/ 1e7 2) 1e7), then adding those parts together. Use future to do both parts at once, and show that this strategy gets the same answer as the single-threaded version, but takes roughly half the time.

(defn sum [start end] (reduce + (range start end)))
(time (let [a (sum 0 (/ 1e7 2)) b (sum (/ 1e7 2) 1e7)]
        (+ a b)))
;; Elapsed time: 946.990875 msecs

(time (let
          [a (future (sum 0 (/ 1e7 2)))
           b (future (sum (/ 1e7 2) 1e7))]
        (+ @a @b)))

;; Elapsed time: 508.677673 msecs

;; 4. Instead of using reduce, store the sum in an atom and use two futures to add each number from the lower and upper range to that atom. Wait for both futures to complete using deref, then check that the atom contains the right number. Is this technique faster or slower than reduce? Why do you think that might be

(defn atomicsum [start end]
  (let [s (atom 0)
        a (let [firsthalf (future (dotimes [i (/ end 2)] (swap! s + i)))]
            @firsthalf)
        b (let [secondhalf (future (dotimes [i (/ end 2)] (swap! s + (+ i (/ end 2)))))]
            @secondhalf)]
    @s))

(time (atomicsum 0 1e7))

;; 4.9999995E13
;; "Elapsed time: 1459.157893 msecs"

;; Clearly using an atom to hold the value and then updating it with futures is more time consuming than just using reduce.

;; 5. Instead of using a lazy list, imagine two threads are removing tasks from a pile of work. Our work pile will be the list of all integers from 0 to 10000:

;; user=> (def work (ref (apply list (range 1e5))))
;; user=> (take 10 @work)
;; (0 1 2 3 4 5 6 7 8 9)

(def work (ref (apply list (range 1e5))))
(def sum (ref 0))

(defn refsum []
  (dosync
   (alter sum + (first @work))
   (alter work rest)
   ))

(while (first @work)
  (let [a (let [fh (future (refsum))]
            @fh)
        b (let [sh (future (refsum))]
            @sh)]
    (when
        (= (rem @sum 10000) 0)
      (prn @sum)
      )))

@work
@sum
