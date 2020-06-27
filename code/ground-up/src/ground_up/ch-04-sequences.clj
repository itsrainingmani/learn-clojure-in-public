(ns ground-up.ch4
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  [& args]
  (println "Welcome to Ch4 of Clojure from the Ground Up"))

;; Chapter 4 - Sequences

(cons 1 [2])
(cons 1 [2 3])
(cons 1 [2 3 4])

;; cons makes a list beginning with the first arg followed by all the elements in 2nd args

(defn inc-first [nums]
  (if (first nums)
    (cons (inc (first nums)) ;; if there is a first element, do cons
          (rest nums))
    (list)))                 ;; else return an empty list


(defn inc-more [nums]
  (if (first nums)
    (cons (inc (first nums))
          (inc-more (rest nums)))
    (list)))

;; Recursion:
;; 1. Some part of the problem which has a known solution
;; 2. A relationship which connects one part of the problem to the next

;; Folding an unbound stream of computation over and over, until only a single step remains

(defn transform-all [f xs]
  (if (first xs)
    (cons (f (first xs))
          (transform-all f (rest xs)))
    (list)))

;; Here we've parametrized this function to take a function and a sequence and apply that function to each x in xs
;; This is the map constructor

;; map, the function relates one sequence to another
;; map, the type relates keys to values

(defn expand [f x count]
  (if (pos? count)
    (cons x (expand f (f x) (dec count)))))

;; we can also use recursion to expand a single value into a sequence of values, each related by some function
;; base case is x, followed by (f x), then (f (f x)) and so on until count is no longer positive
;; This is also the function iterate (iterate returns an infinitely long lazy sequence that we can take from
(take 10 (iterate (fn [x] (if (odd? x) (+ 1 x) (/ x 2))) 10))

;; repeat constructs an infinitely long lazy sequence where every element is the same
(take 5 (repeat "hi"))

;; repeatedly simply calls a function (f) to generate an infinite sequence of values without any relationship between the elements.
;; for ex, we can use repeatedly with rand to make an n-numbered list of random ints
(take 3 (repeatedly rand))

;; cycle - extends a sequence by repeating it forever
(take 10 (cycle [1 2 3]))

;; Transforming sequences
(map (fn [n vehicle] (str "I've got " n " " vehicle "s"))
     [0 200 9]
     ["car" "train" "kiteboard"])

;; Given multiple sequences to map, it calls the function with one element from each sequence in turn
(map +
     [1 2 3]
     [4 5 6]
     [7 8 9]) ;; first value will (+ 1 4 7), second will be (+ 2 5 8) etc.

;; If one sequence is longer than the other, map stops at the end of the smaller one

(map (fn [index element] (str index ". " element))
     (iterate inc 0)             ;; This is infinite
     ["erlang" "ruby" "haskell"]) ;; This is finite. So we stop iterating when this ends

;; Comes with indexes built in
(map-indexed (fn [index element] (str index ". " element))
             ["erlang" "ruby" "haskell"])

(concat [1 2 3] [:a :b :c] [4 5 6])

(interleave [1 2 3] [:a :b :c] [4 5 6]) ;; (1 :a 4 2 :b 5 3 :c 6)

(interpose :and [1 2 3 4]) ;; puts :and in between each element of [1 2 3 4]

(reverse [1 2 3]) ;; [3 2 1]
(reverse "wolf")  ;; (\f \l \o \w)

(->> "abracadabra"
     (seq)
     (shuffle)
     (apply str))

;; Subsequences

;; take -> selects first n elements
;; drop -> removes first n elements
;; take-last -> selects last n elements
;; drop-last -> drops last n elements

;; take-while -> like take but uses a function to decide when to cut
(take-while pos? [3 2 1 0 -1 -2 10]) ;; (3 2 1)
(drop-while pos? [3 2 1 0 -1 -2 10]) ;; (0 -1 -2 10)

;; Cut a sequence at an index using split-at. If the index is greater than the length of the sequence, split-at returns a vector of the input list and an empty list

(split-at 10 (range 6))

(split-with pos? (range 5 -5 -1))

;; filter keeps all elements that match the predicate
(filter pos? [1 5 -3 -5 3 1]) ;; (1 5 3 1)

;; remove removes all elements that match the predicate
(remove string? [1 "turing" :apple]) ;; (1 :apple)

(partition 2 [:cats 5 :bats 3 :mammals 10 :dugongs])
(partition-all 2 [:cats 5 :bats 3 :mammals 10 :dugongs])
(partition-by neg? [1 2 3 4 -1 -2 -3 5 4])

;; partition groups a sequence into chunks. However if the number of elements in the sequence is not evenly divisible by n, partition ignores the overflowing elements.
;; partition-all includes partitions with fewer than n items at the end. We can also supply partition with a pad collection to complete the last partition

;; Collapsing Sequences

;; After transforming a sequence, we want to collapse it to maybe get a smaller value
(frequencies [:meow :marrow :meow :meow])

(pprint (group-by :first [{:first "Li" :last "Zhou"}
                          {:first "Sarah" :last "Lee"}
                          {:first "Sarah" :last "Dunn"}
                          {:first "Li" :last "O'Toole"}]))

;; Map treats each element independently. To reduce a sequence, we need to bring some information along with us. (reduce)

(reduce + [1 2 3 4])

;; Reduce begins by calling (+ 1 2) which yields the state 3. Then it calls (+ 3 3). State is now 6. Finally, it calls (+ 6 4) to get 10. Reduce is like sticking a function between each pair of elements

(reductions + [1 2 3 4]) ;; (1 3 6 10)
;; Reductions allows us to access a sequence of all the intermediate states

(reduce conj #{} [:a :b :b :b :a :a])

;; Reducing elements into a collection has it's own name. into.
;; We can conj [key val] vectors into a map.

(into {} [[:a 2] [:b 3]]) ;; {:a 2, :b 3}

(into '() [1 2 3 4])

(defn my-map [f coll]
  (reduce (fn [output element]
            (conj output (f element)))
          []
          coll))
;; Map can be expressed in form of reduce

(defn my-take-while [f coll]
  (reduce (fn [out elem]
            (if (f elem)
              (conj out elem)
              (reduced out)))
          []
          coll))

;; So can take-while. Here reduced is special function to indicate that our reduction is complete early and can skip the rest of the sequence.

;; lazy-seq allows Clojure to compute a sequence as needs rathern than right away. It defers execution to a later time.

;; We can make a function that incremements every number from 0 to infinity.
(def infseq (map inc (iterate inc 0)))
(realized? infseq) ;; false

;; This function hasn't done any work yet. It is unrealized. It only incremenets the numbers when we ask it to
(take 10 infseq) ;; (1 2 3 4 5 6 7 8 9 10)
(realized? infseq) ;; true

;; lazy sequence cache their contents once evaluated for faster access.

;; Let's use our knowledge to solve a problem. Find the sum of products of consecutive pairs of the first 1000 odd integers.

;; Step 1 - Get 10 ints
(take 10 (iterate inc 0)) ;; (0 1 2 3 4 5 6 7 8 9)

;; Step 2 - Get only the odd numbers

(take 10 (filter odd? (iterate inc 0))) ;; (1 3 5 7 9 11 13 15 17 19)

;; Step 3 - We need to get consecutive pairs. We can use partition for this

(take 3 (partition 2 1 (filter odd? (iterate inc 0)))) ;; ((1 3) (3 5) (5 7))

;; Step 4 - Find the product of each pair using map

(take 3 (map (fn [[a b]] (* a b)) (partition 2 1 (filter odd? (iterate inc 0)))))
;; (3 15 35)

;; Step 5 - Now use reduce to sum it all up
(reduce +
        (take 1000
              (map (fn [[a b]] (* a b))
                   (partition 2 1 (filter odd? (iterate inc 0))))))

;; The part that happens first is nested as the deepest element in this chain of reasoning.
;; We can also express this in order with the ->> macro

(->> 0
     (iterate inc)
     (filter odd?)
     (partition 2 1)
     (map (fn [[a b]] (* a b)))
     (take 1000)
     (reduce +))
;; Data flows in order from top to bottom. This allows us to flatten the deeply nested expression chain into a single level.

;; Exercises
;; 1. Write a function to find out if a string is palindrome or not.
;; Palindrome if s and (reverse s) are equal

(defn palincheck [s]
  (= (seq s) (reverse (seq s))))

;; 2. Find the number of c's in abracadabra
((frequencies (seq "abracadabra")) \c)

;; 3. Write your own version of filter
(defn my-filter [f coll]
  (reduce (fn [out elem]
            (if (f elem)
              (conj out elem)
              out))
          []
          coll))

;; 4. Find the first 100 prime numbers
(defn find-factor
  [n]
  (->> (range 2 (inc n))
       (drop-while #(not= 0 (rem n %)))
       (first)
       ))
