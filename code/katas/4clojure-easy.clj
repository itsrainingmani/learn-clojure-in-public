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
