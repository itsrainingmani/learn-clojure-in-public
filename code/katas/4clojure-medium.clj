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

(seq-rotate 5 [:a :b :c])
