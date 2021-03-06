(ns foreclj-easy
  (:gen-class))

;; Easy Problems on 4Clojure

;; No 19
;; Last Element
;; Write a function which returns the last element in a sequence.
;; (= (__ [1 2 3 4 5]) 5)
;; (= (__ ["b" "c" "d"]) "d")
;; Restriction - last

#(first (reverse %))

;; No 20
;; Penultimate Element
;; Write a function which returns the second to last element in a sequence
;; (= (__ (list 1 2 3 4 5)) 4)
;; (= (__ ["a" "b" "c"]) "b")
;; (= (__ [[1 2] [3 4]]) [1 2])

;; There are many ways of doing this

(fn [seq] (second (reverse seq)))

#(second (reverse %))

#(first (drop (- (count %) 2) %))

;; alaq's sln
(fn penultimate [x] (nth x (- (count x) 2)))

;; No 21
;; Nth Element
;; Write a function which returns the Nth element from a sequence
;; (= (__ [:a :b :c] 0) :a)
;; (= (__ [1 2 3 4] 1) 2)
;; (= (__ '([1 2] [3 4] [5 6]) 2) [5 6])
;; Restriction - nth

(defn newnth [coll ind]
  (if (= 0 ind)
    (first coll)
    (newnth (rest coll) (- ind 1))))

#((vec %1) %2)

(fn [xs n] (first (drop n xs)))


;; No: 22
;; Write a function which returns the total number of elements in a sequence
;; Restriction - count

(fn [coll]
  (#(reduce + (map (fn [x] 1) coll))))

(fn [coll]
  (reduce + (map #(if % 1 0) coll)))

;; No 23
;; Reverse a sequence
;; (= (__ [1 2 3 4 5]) [5 4 3 2 1])
;; (= (__ (sorted-set 5 7 2 7)) '(7 5 2))
;; (= (__ [[1 2][3 4][5 6]]) [[5 6][3 4][1 2]])

;; Brute force method -> 
(fn [xs] (map #(nth xs %) (range (- (count xs) 1) -1 -1)))
;; This won't work on the second test since nth isn't supported on sets

;; Let's a try recursive solution using cons

(defn newrev [xs]
  (if (= (count xs) 2)
    (list (second xs) (first xs))
    (conj (newrev (take (- (count xs) 1) xs)) (last xs))))

;; This is a very contrived way of doing this. Let's break it down
;; Base condition is that if the colleciton has two elements, we flip them
;; This is unnecessary since we can apply conj with just one item to get
;; a list containing that one item
;; I made the assumption that we want to have the end result be a list.
;; This impacted how I approached the problem, since conj behavior on lists
;; is different from how conj works on vectors.

;; This solution by @alaq uses the opposite approach and looks simpler as a result

(fn rev [coll]
  (if (empty? coll)
    []
    (conj (rev (rest coll)) (first coll))))

;; But the really simple solution goes to @banditelol who uses reduce to great effect

(fn rev [coll]
  (reduce (fn [x y] (conj x y)) () coll))

;; No 24
;; Sum It All Up
;; Write a function which returns the sum of a sequence of numbers.
;; (= (__ (list 0 -2 5 5)) 8)
;; (= (__ #{4 2 1}) 7)

#(reduce + %)

;; No 25
;; Find the odd numbers
;; Write a function which returns only the odd numbers from a sequence.
;; (= (__ #{1 2 3 4 5}) '(1 3 5))

#(filter odd? %)

;; No 26
;; Fibonacci Sequence

(comment
  (defn fibonacci [n]
    (reduce (fn [fb y] ((let [x1 (first (last fb))]) conj fb [(+ x1 x2)])) [[0 1]] (range 20))))

(reduce (fn [fb y]
          (let [c  (last fb)
                x1 (first c)
                x2 (second c)]
            (conj fb '(x2 (+ x1 x2)))))
        ['(0 1)]
        (range 20))

((fn [fb]
   (let [c  (last fb)
         x1 (first c)
         x2 (second c)])
   (conj fb '(x2 (+ x1 x2)))) ['(0 1)])

;; Soln:
#(take % (map second (iterate (fn [[x1 x2]] [x2 (+ x1 x2)]) [0 1])))

;; No 38
;; Maximum Value
;; Write a function that takes a variable number of arguments and returns the maximum value

(fn [& args] (reduce (fn [acc y] (if (> y acc) y acc)) Long/MIN_VALUE args))

;; No 27
;; Palindrome Detector
;; Input may not necessarily be a sequence

#(= (seq %) (reverse (seq %)))

;; No 28
;; Get Caps
;; Given a string, get only the capitalized characters

#(apply str (remove empty? (re-seq #"[A-Z]*" %)))

;; No 48
;; Intro to some
;; The some function takes a predicate function and a collection. It returns the first logical true value of (predicate x) where x is an item in the collection.
;; (= __ (some #{2 7 6} [5 6 7 8]))
;; (= __ (some #(when (even? %) %) [5 6 7 8]))

6

;; No 49
;; Split a sequence
;; Write a function which will split a sequence into two parts.
;; (= (__ 3 [1 2 3 4 5 6]) [[1 2 3] [4 5 6]])
;; (= (__ 1 [:a :b :c :d]) [[:a] [:b :c :d]])

#(vector (take %1 %2) (take-last (- (count %2) %1) %2))

;; No 34
;; Implement range
;; Write a function which creates a list of all integers in a given range.
;; (= (__ 1 4) '(1 2 3))
;; (= (__ -2 2) '(-2 -1 0 1))

#(take (- %2 %1) (iterate inc %1))

;; No 83
;; A Half-Truth

(>= (get (frequencies [false true]) true) 1)
(not= (get (frequencies [false true]) false 0) 0)

;; (and (>= (get (frequencies xs) true 0) 1) (not= (get (frequencies xs) false 0) 0))



;; No 83
;; A Half Truth
;; REP
;; Read - Variable number of booleans
;; Print - true false
;; Eval - if every boolean is true or false return false
;; else, return true

;; Count number of trues and number of falses

;; #_ for commenting out code

(#(not= (count (partition-by identity (sort %&))) 1) '(true false false true))

;; Boiled down the problem statement into a boolean expression
;; (count of true >= 1 && count of false != 0)

;; how do i get counts of true and false from the input list
;; (frequencies %&) -> {false: 1, true: 3}

;; Then get the counts from this map.
;; Use an and expression to evaluate this

(#(and (>= (get (frequencies %&) true 0) 1) (not= (get (frequencies %&) false 0) 0)) false false true)

(not= 1
      (count
       (partition-by identity
                     (sort
                      '(false true false)))))

;; Simplest solution
#(not= %&)


;; not= => (not (= false true false))
;; the = function checks if all the given arguments are the same
;; not= flips whatever the answer to = is
;; No 83 can be reduced to -> If the input is all true or all false. Return false. Else true.

;; Self-Feedback
;; It's ok to not get it.

;; 30. Compress a sequence
;; Write a function which removes consecutive duplicates from a sequence

;; (= (__ [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))

#(map first (partition-by identity %1))

;; 31. Pack a Sequence
;; Write a function which packs consecutive duplicates into sub-lists
;; (= (__ [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))

#(partition-by identity %1)

;; 32. Duplicate a Sequence
;; Write a function which duplicates each element of a sequence

#(interleave %1 %1)

;; 33. Replicate a Sequence
;; Write a function which replicates each element of a sequence a variable number of times
;; (= (__ [:a :b] 4) '(:a :a :a :a :b :b :b :b))
;; (= (__ [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4]))

#(apply concat (map (fn [x] (repeat %2 x)) %1))
#(mapcat (fn [x] (repeat %2 x)) %1)

;; 39. Interleave Two Seqs
;; Write a function which takes two sequences and returns the first item from each, then the second item from each etc..

#(apply concat (map (fn [x y] [x y]) %1 %2))

;; 42. Factorial Fun
;; Write a function which calculates factorials

#(reduce * (range 1 (+ 1 %1)))

;; 42. Reverse Interleave
;; Write a function which reverses the interleave process into x number of subsequences
;; (= (__ [1 2 3 4 5 6] 2) '((1 3 5) (2 4 6)))

;; 28. Flatten a sequence
;; Write a function which flattens a sequence
;; (= (__ '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6))

"TODO"

(defn new-flatten [xs]
  (if (some coll? xs)
    (new-flatten (mapcat (fn [x] (if-not (coll? x) (vector x) x)) xs))
    xs))

(defn new-flatten-recurse
  "Solve the problem with recursion"
  [xs]
  (if (first xs)
    (concat (if (coll? (first xs))
              (new-flatten-recurse (first xs))
              (list (first xs)))
            (new-flatten-recurse (rest xs)))
    (list)))

;; 45. Intro to Iterate
;; The iterate function can be used to produce an infinite lazy sequence
;; (= __ (take 5 (iterate #(+ 3 %) 1)))

'(1 4 7 10 13)

;; 47. Contain yourself
;; The contains? function checks if a KEY is present in a given collection. This often leads beginner clojurians to use it incorrectly with numerically indexed collections like vectors and lists.
;; (contains? #{4 5 6} __)
;; (contains? [1 1 1 1 1] __)
;; (contains? {4 :a 2 :b} __)

4

;; 41. Drop Every Nth Item
;; Write a function which drops every Nth item from a sequence.
;; (= (__ [:a :b :c :d :e :f] 2) [:a :c :e])
;; (= (__ [1 2 3 4 5 6 7 8] 3) [1 2 4 5 7 8])

#(remove nil? (map-indexed (fn [i x] (when-not (= (rem (+ i 1) %2) 0) x)) %1))

;; 61. Map Construction
;; Write a function which takes a vector of keys and vector of values and constructs a map from them

(defn new-zip [x y]
  (reduce (fn [m [k v]] (assoc m k v)) {} (partition 2 (interleave x y))))

(new-zip [:a :b :c] [1 2 3])

;; 81. Set Intersection
;; Write a function which returns the intersecton of two sets. The intersection is the sub-set of items that each set has in common.

(defn new-intersect
  [s1 s2]
  (into #{} (remove nil? (map (fn [x] (when (contains? s2 x) x)) s1))))

(new-intersect #{:a :b :c :d} #{:c :e :a :f :d})

;; 66. Greatest Common Divisor

(defn gcd [& args]
  (let [[x y] (sort > args)]
    (if (zero? y)
      x
      (gcd y (rem x y)))))

(gcd 1023 858) ;; => 33

;; 107. Simple Closures
;; Given a positive integer n, return a function (f x) which computes xn. Observe that the effect of this is to preserve the value of n for use outside the scope in which it is defined.
;; (= [1 8 27 64] (map (__ 3) [1 2 3 4]))

(defn power [n]
  (fn [x] (int (Math/pow x n))))

;; 62. Re-implement Iterate
;; Given a side-effect free function f and an initial value x, write a function which returns an infinite lazy sequence of x, (f x), (f (f x)) etc.
;; (= (take 5 (__ #(* 2 %) 1)) [1 2 4 8 16])

(defn new-iterate [f x]
  (lazy-seq
   (cons x (new-iterate f (f x)))))

;; 166. Comparisions
;; Write a function that takes three arguments, a less than operator for the data and two items to compare. The function should return a keyword describing the relationship between the two items. The keywords for the relationship between x and y are as follows:
;; x = y → :eq
;; x > y → :gt
;; x < y → :lt

(defn comparisons [op x y]
  (if (= x y)
    :eq
    (if (op x y)
      :lt
      :gt)))

(fn cmp
  [op x y]
  (cond
    (op x y) :lt
    (op y x) :gt
    :else :eq))

;; 90. Cartesian Product
;; Write a function which calculates the Cartesian product of two sets.
;; (= (__ #{1 2 3} #{4 5})
;; #{[1 4] [2 4] [3 4] [1 5] [2 5] [3 5]}

(defn cartesian-prod [x y]
  (into #{} (for [coll1 x
                  coll2 y]
              [coll1 coll2])))

;; 99. Product Digits
;; Write a function which multiplies two numbers and returns the result as a sequence of it's digits
;; (= (__ 999 99) [9 8 9 0 1])

(defn prod-digits [x y]
  (map #(Integer/parseInt (str %1)) (str (* x y))))

;; 63. Group By
;; Given a function f and a sequence s, write a function which returns a map. The keys should be the values of f applied to each item in s. The value at each key should be a vector of corresponding items in the order they appear in s.
;; (= (__ #(> % 5) [1 3 6 8]) {false [1 3], true [6 8]})

(defn new-group-by [f s]
  (let [m {}]
    (reduce
     (fn [acc v]
       (let [applied (f v)]
         (if-let [k (get acc applied)]
           (assoc acc applied (conj k v))
           (assoc acc applied [v]))))
     m s)))


;; 88. Symmetric Difference
;; Write a function which returns the symmetric difference of two sets. The symmetric difference is the set of items belonging to one but not both of the two sets.

(defn sym-diff [a b]
  (->> (concat a b)
       (sort)
       (partition-by identity)
       (filter (fn [x] (= (count x) 1)))
       (apply concat)
       (into #{})))

;; 43. dot product
;; Create a function that computes the dot product of two sequences
;; (= 0 (__ [0 1 0] [1 0 0]))
;; (= 32 (__ [1 2 3] [4 5 6]))

(defn dot-prod [x y]
  (reduce + (map * x y)))

;; anon fn -
#(reduce + (map * %1 %2))

;; 122. Read a Binary Number
;; Convert a binary number, provided in the form of a string, to its numerical value.

(defn bin-to-dec [s]
  (->> s             ;; "1010"
       (reverse)     ;; "0101"
       (seq)         #_'(\0 1 \0 \1)
       (map str)     #_'("0" "1" "0" "1")
       (map #(Integer/parseInt %1))  #_(0 1 0 1)
       (zipmap (take (count s) (iterate (fn [x] (* 2 x)) 1))) #_{1 0, 2 1, 4 0, 8 1}
       (reduce (fn [acc [k v]] (+ acc (* k v))) 0))) #_10

;; 40. Interpose a Seq

(defn new-interpose
  [i xs]
  (drop-last (mapcat #(list %1 i) xs)))

;; 135. Infix Calculator
;; Your friend Joe is always whining about Lisps using the prefix notation for math. Show him how you could easily write a function that does math using the infix notation. Is your favorite language that flexible, Joe? Write a function that accepts a variable length mathematical expression consisting of numbers and the operations +, -, *, and /. Assume a simple calculator that does not do precedence and instead just calculates left to right.
;; (= 42 (__ 38 + 48 - 2 / 2))
;; (= 72 (__ 20 / 2 + 2 + 4 + 8 - 6 - 10 * 9))

(defn infix-calc
  [& xs]
  (reduce (fn [acc v]
            (if (fn? v)
              (partial v acc)
              (acc v))) (partial + 0) xs))

(infix-calc 38 + 48 - 2)
(infix-calc 38 + 48 - 2 / 2)
(infix-calc 10 / 2 - 1 * 2)
(infix-calc 20 / 2 + 2 + 4 + 8 - 6 - 10 * 9)

;; 118. Re-implement Map
;; Map is one of the core elements of a functional programming language. Given a function f and an input sequence s, return a lazy sequence of (f x) for each element x in s.

"TODO"

(defn new-map
  [f s]
  (lazy-seq
   (reduce
    (fn [acc v]
      (conj acc (f v)))
    [] s)))

(new-map inc [3 4 5 6])
(new-map (fn [_] nil) (range 10))

;; 157. Indexing Sequences
;; Transform a sequence into a sequence of pairs containing the original elements along with their index.

(defn index-seq
  [xs]
  (map #(list %1 %2)  xs (range 0 (count xs))))

(index-seq [:a :b :c])

;; 95. To Tree, or not to Tree
;; Write a predicate which checks whether or not a given sequence represents a binary tree. Each node in the tree must have a value, a left child, and a right child.

(defn istree?
  [tree]
  (if (coll? tree)
    (if (= (count tree) 3)
      (and (istree? (second tree)) (istree? (nth tree 2)))
      false)
    (nil? tree)))

(istree? [1 nil [2 [3 nil nil] [4 nil nil]]])

;; No. 118 Re-implement Map
;; Map is one of the core elements of a functional programming language. Given a function f and an input sequence s, return a lazy sequence of (f x) for each element x in s.
;; (= [3 4 5 6 7]
      ;; (__ inc [2 3 4 5 6]))

(defn reimplement-map
  [f s]
  (if-not (nil? (next s))
    (lazy-seq (cons (f (first s)) (reimplement-map f (rest s))))
    (list (f (first s)))))

(reimplement-map inc [2 3 4 5 6])
(reimplement-map (fn [_] nil) (range 10))

;; No. 128 Recognize Playing Cards
;; Write a function which converts (for example) the string "SJ" into a map of {:suit :spade, :rank 9}. A ten will always be represented with the single character "T", rather than the two characters "10".

;; (= {:suit :diamond :rank 10} (__ "DQ"))

(defn cards
  [c]
  (let [info {}
        f (first c)
        s (second c)
        iparse #(Integer/parseInt (str %))]
    (-> info
        (assoc :suit
               (case f
                 \S :spade
                 \C :club
                 \H :heart
                 \D :diamond))
        (assoc :rank
               (case s
                 \T 8
                 \J 9
                 \Q 10
                 \K 11
                 \A 12
                 (- (iparse s) 2))))))

(cards "CA")

;; 97. Pascal's Triangle

(defn pascalize
  ([n] (pascalize (dec n) '(1)))
  ([n p]
   (if (zero? n)
     p
     (recur (dec n) (conj (map #(apply + %)  (partition-all 2 1 p)) 1)))))

;; No. 147 Pascal's Trapezoid
;; Write a function that, for any given input vector of numbers, returns an infinite lazy sequence of vectors, where each next one is constructed from the previous following the rules used in Pascal's Triangle. For example, for [3 1 2], the next row is [3 4 3 2].

(defn pascal-trap
  [s]
  (let [pscl (fn [n] (cons (first n) (map #(apply +' %) (partition-all 2 1 n))))]
    (cons s
          (lazy-seq (pascal-trap (pscl s))))))

;; No. 146
;; Trees into tables

(defn t-to-t [m]
(into {} (mapcat (fn [[k mm]]
                   (for [y mm]
                     [[k (first y)] (second y)])) m)))

;; No. 51 Advanced Destructuring
;; (= [1 2 [3 4 5] [1 2 3 4 5]] (let [[a b & c :as d] __] [a b c d]))

[1 2 3 4 5]

;; No. 96 Beauty is Symmetry
;; Let us define a binary tree as "symmetric" if the left half of the tree is the mirror image of the right half of the tree. Write a predicate to determine whether or not a given binary tree is symmetric. (see To Tree, or not to Tree for a reminder on the tree representation we're using).

(defn tree-sym?
[tree]
(let [rt (fn rev-tree [t]
           (if (coll? t)
             (list (first t) (rev-tree (nth t 2)) (rev-tree (second t)))
             t))]
  (= (second tree) (rt (nth tree 2)))))

(tree-sym? '(:a (:b nil nil) nil))

;; No. 126 Through the Looking Class
;; Enter a value which satisfies the following:
;; (let [x __]
;;   (and (= (class x) x) x))

Class

;; No. 173 Intro to Destructuring 2
;; Sequential destructuring allows you to bind symbols to parts of sequential things (vectors, lists, seqs, etc.): (let [bindings* ] exprs*) Complete the bindings so all let-parts evaluate to 3.
;; (= 3
;;   (let [[__] [+ (range 3)]] (apply __))
;;   (let [[[__] b] [[+ 1] 2]] (__ b))
;;   (let [[__] [inc 2]] (__)))

;; a c

;; No. 120 Sum of square of digits

(defn sum-of-square
  [xs]
  (let [make-digit (fn [n] (map #(Integer/valueOf (str %)) (String/valueOf n)))
        sqr-digit (fn [n] (apply + (map #(* % %) (make-digit n))))]
    (count (filter #(> (sqr-digit %) %) xs))))

;; (sum-of-square (range 100))

;; No. 153 Pairwise Disjoint Sets
;; Given a set of sets, create a function which returns true if no two of those sets have any elements in common1 and false otherwise. Some of the test cases are a bit tricky, so pay a little more attention to them.

(defn pairwise
  [xs]
  (let [ls (for [x xs y xs :when (not= x y)] [x y])]
    (apply = 0 (map #(count (clojure.set/intersection (first %) (second %))) ls))))

;; (pairwise #{#{'(:x :y :z) '(:x :y) '(:z) '()}
;; #{#{:x :y :z} #{:x :y} #{:z} #{}}
;; #{'[:x :y :z] [:x :y] [:z] [] {}}})

;; No. 100 Least Common Multiple
;; Write a function which calculates the least common multiple. Your function should accept a variable number of positive integers or ratios.

(defn lcm
  ([x y & args]
   (reduce lcm (conj args y x)))
  ([x y]
   (let [gcd (fn [a b] (if (zero? b) a (recur b (rem a b))))]
     (/ (* x y) (gcd x y)))))

;; (lcm 1/3 2/5)
