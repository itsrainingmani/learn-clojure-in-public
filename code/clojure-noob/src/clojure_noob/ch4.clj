(ns clojure-noob.ch4
  (:gen-class))

;; If you can perform all of an abstraction’s operations on an object, then that object is an instance of the abstraction
;; 
;; A sequence is a collection of elements organized in linear order vs an unordered collection or a graph without a before-after
;; relationship between its nodes
;; 
;; Implements functions in terms of sequence abstractions. map, reduce take a seq.
;; 
;; If the core sequence functions (first, rest and cons) work on a DS, it implements the sequence abstraction.
;; 
(defn titleize
  [topic]
  (str topic " for the Brave and True"))

;; vectors
(map titleize ["Hamsters" "ragnarok"])
;; => ("Hamsters for the Brave and True" "ragnarok for the Brave and True")

;; list
(map titleize '("Table Tennis" "Empathy"))
;; => ("Table Tennis for the Brave and True" "Empathy for the Brave and True")

;; set
(map titleize #{"Hamsters" "ragnarok"})
;; => ("Hamsters for the Brave and True" "ragnarok for the Brave and True")

;; Abstraction through indirection
;; Indirection is a term for a mechanism that a programming language employs so that one name can have multiple, related meanings
;; Polymorphic functions dispatch to different function bodies based on the type of the argument supplied.
;; 
;; Whenever a Clojure function expects a seq, it uses the seq function on the data structure in order to obtain a data structure that
;; allows for first, rest and cons
;; 
;; seq always returns a value that looks like a list
;; 
(seq {:name "Billy" :age 43})
;; => ([:name "Billy"] [:age 43])
;; We get a list of two element vectors

(def human-consumption [8.1 8.3 4.4 6.1])
(def critter-consumption [0.0 0.2 0.3 1.3])
(defn unify-diet
  [human critter]
  {:human human :critter critter})

(map unify-diet human-consumption critter-consumption)
;; => ({:human 8.1, :critter 0.0} {:human 8.3, :critter 0.2} {:human 4.4, :critter 0.3} {:human 6.1, :critter 1.3})

;; Use map to retrieve the value associated with a keyword from a collection of map data structures.
;; Because keywords can be used as functions
;; 
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spiderman" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)
;; => ("Bruce Wayne" "Peter Parker" "Your mom" "Your dad")

;; Passing a collection of functions as an argument
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [1 2 3 4])
;; => (10 4 5/2)

(stats [80 1 44 13 6])
;; => (144 5 144/5)

;; Reduce
;; Reduce processes each element in a sequence to build a result. The result does not have to a single value.
;; You can use reduce to build a larger sequence from a smaller one as well.
;; 
(reduce (fn [new-map [key val]] (assoc new-map key (inc val))) {} {:max 30 :min 10})
;; => {:max 31, :min 11}
;; 
;; (def food-journal
     [{:month 1 :day 1 :human 5.3 :critter 2.3}
      {:month 1 :day 2 :human 5.1 :critter 2.0}
      {:month 2 :day 1 :human 4.9 :critter 2.1}
      {:month 2 :day 2 :human 5.0 :critter 2.5}
      {:month 3 :day 1 :human 4.2 :critter 3.3}
      {:month 3 :day 2 :human 4.0 :critter 3.8}
      {:month 4 :day 1 :human 3.7 :critter 3.9}
      {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)
;; => ({:month 1, :day 1, :human 5.3, :critter 2.3}
;;     {:month 1, :day 2, :human 5.1, :critter 2.0}
;;     {:month 2, :day 1, :human 4.9, :critter 2.1}
;;     {:month 2, :day 2, :human 5.0, :critter 2.5})

;; => ({:month 1, :day 1, :human 5.3, :critter 2.3}
;;     {:month 1, :day 2, :human 5.1, :critter 2.0}
;;     {:month 2, :day 1, :human 4.9, :critter 2.1}
;;     {:month 2, :day 2, :human 5.0, :critter 2.5})

;; take while the predicate returns true
;; drop while the predicate returns true
;; filter returns all elements of a sequence that tests true for predicate
;; some returns the first truthy value that satisfies the predicate and nil otherwise
;; 
(sort [3 2 1])
;; => (1 2 3)

(sort-by count ["aaa" "c" "vv"])
;; => ("c" "vv" "aaa")

(concat [1 2] [3 4])
;; => (1 2 3 4)

;; Use reduce to derive a new value from a seq-able data structure.
;; 
;; Demonstrating Lazy Seq Efficiency
(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [ssn]
  (Thread/sleep 1000)
  (get vampire-database ssn))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identity-vampire
  [ssns]
  (first (filter vampire?
                 (map vampire-related-details ssns))))

(time (vampire-related-details 0))
;; takes 1 second
;; => {:makes-blood-puns? false, :has-pulse? true, :name "McFishwich"}

;; lazy seq is a seq whose members aren't computed until you try to access them
;; 
;; Deferring the computation until it's needed makes the program more efficient and allows you to construct infinite sequences
;; 

(time (def mapped-details (map vampire-related-details (range 0 1000000))))
;; "Elapsed time: 0.042193 msecs"
;; #'clojure-noob.ch4/mapped-details
;; 
;; Lazy Seq - recipe for how to realize the elements of a sequence and the elements that have been realized so far. Everytime you try to access an unrealized element, the lazy seq will use its recipe to generate the requested element.
;; 
(time (first mapped-details))
;; => "Elapsed time: 32030.767 msecs"
;; => {:makes-blood-puns? false, :has-pulse? true, :name "McFishwich"}

;; Took 32 seconds because when Clojure has to realize an element, it realizes the next 31 elements as well pre-emptively. This is done for better performance
;; 
;; Accessing (first mapped-details) again takes almost no time since the results are cached
;; 
(concat (take 8 (repeat "na")) ["Batman!"])
;; => ("na" "na" "na" "na" "na" "na" "na" "na" "Batman!")

(take 3 (repeatedly (fn [] (rand-int 10))))
;; => (2 9 4)

(defn even-nums
  ([] (even-nums 0))
  ([n] (cons n (lazy-seq (even-nums (+ n 2))))))

(take 10 (even-nums))
;; => (0 2 4 6 8 10 12 14 16 18)

(cons 0 '(2 4 6))
;; => (0 2 4 6)

;; Collection Abstraction
;; The sequence absrtaction is about operating on members individually, whereas the collection abstraction is about the data structure as a whole. For eg. the collection functions count, empty? and every?.
;; 
(empty? [])
;; => true

(empty? ["no!"])
;; => false

;; into converts a sequence into another type
;; 
(map identity {:sunlight "Glitter"})
;; => ([:sunlight "Glitter"])

(into {} (map identity {:sunlight "Glitter"}))
;; => {:sunlight "Glitter"}

(into {:favorite-animal "kitty"} {:least-favorite-smell "dog"
                                  :relationship-with-teenager "creepy"})
;; => {:favorite-animal "kitty", :least-favorite-smell "dog", :relationship-with-teenager "creepy"}

;; into basically takes two collections and adds all elements from the second to the first
;; 
;; Function Functions
;; 
;; Clojure can accept and return functions as function args
;; 
;; apply explodes a seqable data structure so it can be passed to a function that expects a rest parameter
;; 
;; 
(apply max [0 1 2])
;; => 2
;; 
;; max expects a number of args, not a seqable data structure
;; (apply max [0 1 2]) is equivalent to (max 0 1 2)
;; 
;; partial takes a function and any number of args and returns a new function. it forms a closure with the originally supplied args and you can call this function with new args
;; 
(def add10 (partial + 10))
(add10 3)
;; => 13

(add10 5)
;; => 15

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))

(add-missing-elements "fire")
;; => ["water" "earth" "air" "fire"]

(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))

(def add20 (my-partial + 20))
(add20 5)
;; => 25

;; complement - takes a fn f and returns a fn that takes the same args as f, has the same effects, but returns the opposite truth value
;; 
(defn iseven?
  [n]
  (prn (str "Given number is: " n))
  (= (rem n 2) 0))

(def isodd? (complement iseven?))

(iseven? 20)
(isodd? 21)