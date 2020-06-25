;; No 1
;; Nothing but the truth
;; This is a clojure form. Enter a value which will make the form evaluate to true. Don't over think it!
;; (= __ true)

(= 69 69)

;; No 2
;; Simple Math
;; (= (- 10 (* 2 3)) __)

4

;; No 3
;; Intro to Strings
;; Clojure strings are Java strings. This means that you can use any of the Java string methods on Clojure strings.
;; (= __ (.toUpperCase "hello world"))

"HELLO WORLD"

;; No 4
;; Intro to Lists
;; Lists can be constructed with either a function or a quoted form.
;; (= (list __) '(:a :b :c))

:a :b :c

;; No 5
;; Lists: conj
;; When operating on a list, the conj function will return a new list with one or more items "added" to the front.
;; (= __ (conj '(2 3 4) 1))
;; (= __ (conj '(3 4) 2 1))

'(1 2 3 4)

;; No 6
;; Intro to Vectors
;; Vectors can be constructed several ways. You can compare them with lists.
;; (= [__] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))

:a :b :c

;; No 7
;; Vectors: conj
;; (= __ (conj [1 2 3] 4))
;; (= __ (conj [1 2] 3 4))

[1 2 3 4]

;; No 8
;; Intro to Sets
;; Sets are collections of unique values.
;; (= __ (set '(:a :a :b :c :c :c :c :d :d)))
;; (= __ (clojure.set/union #{:a :b :c} #{:b :c :d}))

#{:a :b :c :d}

;; No 9
;; Sets: conj
;; When operating on a set, the conj function returns a new set with one or more keys "added".
;; (= #{1 2 3 4} (conj #{1 4 3} __))

2

;; No 10
;; Intro to Maps
;; (= __ ((hash-map :a 10, :b 20, :c 30) :b))
;; (= __ (:b {:a 10, :b 20, :c 30}))

20

;; No 11
;; Maps: conj
;; When operating on a map, the conj function returns a new map with one or more key-value pairs "added".
;; (= {:a 1, :b 2, :c 3} (conj {:a 1} __ [:c 3]))

[:b 2]

;; No 12
;; Intro to Sequences
;; All Clojure collections support sequencing. You can operate on sequences with functions like first, second, and last.
;; (= __ (first '(3 2 1)))
;; (= __ (second [2 3 4]))
;; (= __ (last (list 1 2 3)))

3

;; No 13
;; Sequences: rest
;; The rest function will return all the items of a sequence except the first
;; (= __ (rest [10 20 30 40]))

[20 30 40]

;; No 14
;; Intro to Functions
;; Clojure has many different ways to create functions.
;; (= __ ((fn add-five [x] (+ x 5)) 3))
;; (= __ ((fn [x] (+ x 5)) 3))
;; (= __ (#(+ % 5) 3))
;; (= __ ((partial + 5) 3))

8

;; No 15
;; Double Down
;; Write a function that doubles a number
;; (= (__ 2) 4)

(partial * 2)

;; No 16
;; Hello World
;; Write a function which returns a personalized greeting
;; (= (__ "Mani") "Hello, Mani!")

(fn [name] (str "Hello, " name "!"))

;; No 17
;; Sequences: map
;; The map function takes two arguments: a function (f) and a sequence (s). Map returns a new sequence consisting of the result of applying f to each item of s. Do not confuse the map function with the map data structure.
;; (= __ (map #(+ % 5) '(1 2 3)))

'(6 7 8)

;; No 18
;; Sequences: filter
;; The filter function takes two arguments: a predicate function (f) and a sequence (s). Filter returns a new sequence consisting of all the items of s for which (f item) returns true.
;; (= __ (filter #(> % 5) '(3 4 5 6 7)))

'(6 7)

;; No 35
;; Local bindings
;; Clojure lets you give local names to values using the special let-form.
;; (= __ (let [x 3, y 10] (- y x)))
;; (= __ (let [x 5] (+ 2 x)))

(int (Math/sqrt 49))

;; No 36
;; Let it Be
;; Can you bind x, y, and z so that these are all true?
;; (= 10 (let __ (+ x y)))
;; (= 4 (let __ (+ y z)))
;; (= 1 (let __ z))

[x 7, y 3, z 1]

;; No 37
;; Regular Expressions
;; (= __ (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))

"ABC"

;; No 64
;; Intro to Reduce
;; (= 15 (reduce __ [1 2 3 4 5]))
;; (=  0 (reduce __ []))

+

;; No 57
;; Simple Recursion
;; (= __ ((fn foo [x] (when (> x 0) (conj (foo (dec x)) x))) 5))

'(5 4 3 2 1)

;; No 156
;; When retrieving values from a map, you can specify default values in case the key is not found:
;; (= 2 (:foo {:bar 0, :baz 1} 2))
;; However, what if you want the map itself to contain the default values? Write a function which takes a default value and a sequence of keys and constructs a map.
;; (= (__ 0 [:a :b :c]) {:a 0 :b 0 :c 0})
;; (= (__ "x" [1 2 3]) {1 "x" 2 "x" 3 "x"})

(fn def-assoc [dflt xs] (#(reduce (fn [mp k] (assoc mp k dflt)) {} xs)))

;; No 71
;; Rearranging Code
;; The -> macro threads an expression x through a variable number of forms. First, x is inserted as the second item in the first form, making a list of it if it is not a list already. Then the first form is inserted as the second item in the second form, making a list of that form if necessary. This process continues for all the forms. Using -> can sometimes make your code more readable.

(sort (rest (reverse [2 5 4 1 3 6])))

(-> [2 5 4 1 3 6]
    (reverse)
    (rest)
    (sort))

;; No 72
;; Rearranging code ->>

(->> [2 5 4 1 3 6]
     (drop 2)
     (take 3)
     (map inc)
     (reduce +)) ;; => 11

;; No 134
;; a nil key
;; Write a function which given a key and a map, returns true iff (if and only if) the map contains the key and the value if nil
;; (true?  (__ :a {:a nil :b 2}))
;; (false? (__ :b {:a nil :b 2}))
;; (false? (__ :c {:a nil :b 2}))

(defn iff [k m] (and (contains? m k) (nil? (k m))))

;; No 145
;; For the win
;; Clojure's for macro is a tremendously versatile mechanism for producing a sequence based on some other sequence(s)

;; (= __ (for [x (range 40)
;;             :when (= 1 (rem x 4))]
;;        x))

;; (= __ (for [x (iterate #(+ 4 %) 0)
;;             :let [z (inc x)]
;;             :while (< z 40)
;;        z))

'(1 5 9 13 17 21 25 29 33 37)

;; No 52
;; Intro to Desctructuring
;; Let bindings and function parameter lists support desctructuring
;; (= [2 4] (let [[a b c d e] [0 1 2 3 4]] __))

[2 4]

;; No 162
;; Logical falsity and truth
;; In Clojure, only nil and false represent the values of logical falsity in conditional tests

1

;; No 161
;; Subset and Superset
;; Set A is a subset of set B, or equivalently B is a superset of A, if A is "contained" inside B. A and B may coincide.
;; (clojure.set/superset? __ #{2})
;; (clojure.set/subset? #{1} __)
;; (clojure.set/superset? __ #{1 2})
;; (clojure.set/subset? #{1 2} __)

#{1 2}

;; No 68
;; Recurring Theme
;; Clojure only has one non-stack-consuming looping construct: recur. Either a function or a loop can be used as the recursion point. Either way, recur rebinds the bindings of the recursion point to the values it is passed. Recur must be called from the tail-position, and calling it elsewhere will result in an error.

(loop [x      5
       result []]
  (if (> x 0)
    (recur (dec x) (conj result (+ 2 x)))
    result))

;; [7 6 5 4 3]
