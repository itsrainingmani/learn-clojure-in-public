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
