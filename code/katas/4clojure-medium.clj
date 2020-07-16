(ns foreclj-medium
  (:gen-class))

;; No. 43 Reverse Interleave
;; Write a function which reverses the interleave process into x number of subsequences.

(fn [s n]
  (map
   (fn [i] (map #(nth % i) (partition n n s)))
   (range n)))

;; No. 44 Rotate Sequence
;; Write a function which can rotate a sequence in either direction.

(defn seq-rotate [n s]
  (let [m (mod n (count s))]
    (->> s
         (split-at m)
         reverse
         (apply concat))))

;; (seq-rotate 5 [:a :b :c])
;; => (:c :a :b)

;; No. 50 Split by Type
;; Write a function which takes a sequence consisting of items with different types and splits them up into a set of homogeneous sub-sequences. The internal order of each sub-sequence should be maintained, but the sub-sequences themselves can be returned in any order (this is why 'set' is used in the test cases).

(defn spl-by-type
  [xs]
  (into #{}
        (vals
         (reduce
          (fn [acc v]
            (assoc acc (type v)
                   (if-let [x (get acc (type v))]
                     (conj x v)
                     [v])))
          {} xs))))

;; (spl-by-type [1 :a 2 :b 3 :c])
;; => #{[:a :b :c] [1 2 3]}

;; No. 55 Count Occurences
;; Write a function which returns a map containing the number of occurences of each distinct item in a sequence.

(defn occur
  [xs]
  (->> xs
       distinct
       (reduce (fn [acc a] (assoc acc a (count (filter #(= a %) xs)))) {})))

;; (occur [1 1 2 3 2 1 1])
;; => {1 4, 2 2, 3 1}

;; No. 54 Partition a Sequence
;; Write a function which returns a sequence of lists of x items each. Lists of less than x items should not be returned.

(defn my-partition
  ([n x] (my-partition n x []))
  ([n x y]
   (let [fpl (take n x)
         spl (drop n x)]
     (if (> n (count spl))
       (conj y fpl)
       (my-partition n spl (conj y fpl))))))

;; (my-partition 3 (range 8))
;; => [(0 1 2) (3 4 5)]

;; No. 74 Filter Perfect Squares
;; Given a string of comma separated integers, write a function which returns a new comma separated string that only contains the numbers which are perfect squares.

(defn filter-sq
  [s]
  (let [xs (clojure.string/split s #",")
        ms (map #(Integer/valueOf %) xs)]
    (->> ms
         (filter (fn [x]
                   (let [sqra (Math/sqrt x)]
                     (== sqra (int sqra)))))
         (clojure.string/join #","))))

;; (filter-sq "4,5,6,7,8,9")
;; => "4,9"
