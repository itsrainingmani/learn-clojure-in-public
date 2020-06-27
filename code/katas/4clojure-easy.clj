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
