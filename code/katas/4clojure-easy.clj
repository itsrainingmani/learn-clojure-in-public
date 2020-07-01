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
    (reduce (fn [fb y] ((let [x1 (first (last fb))])conj fb [ (+ x1 x2)])) [[0 1]] (range 20))))

(reduce (fn [fb y]
          (let [c  (last fb),
                x1 (first c),
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

(and (>= (get (frequencies xs) true 0) 1) (not= (get (frequencies xs) false 0) 0))



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
           (assoc acc applied [v])))
       )
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
       (seq)         #_ '(\0 1 \0 \1)
       (map str)     #_ '("0" "1" "0" "1")
       (map #(Integer/parseInt %1))  #_ (0 1 0 1)
       (zipmap (take (count s) (iterate (fn [x] (* 2 x)) 1))) #_ {1 0, 2 1, 4 0, 8 1}
       (reduce (fn [acc [k v]] (+ acc (* k v))) 0))) #_ 10
