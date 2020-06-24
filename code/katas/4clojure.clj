;; 4Clojure Problem 22
;; Write a function which returns the total number of elements in a sequence

(defn ct [coll] (#(reduce + (map (fn [x] 1) coll))))

(println (ct '(1 2 3 4))) ;; should return 4
