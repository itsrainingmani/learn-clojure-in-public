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
(= (__ [[1 2][3 4][5 6]]) [[5 6][3 4][1 2]])

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
