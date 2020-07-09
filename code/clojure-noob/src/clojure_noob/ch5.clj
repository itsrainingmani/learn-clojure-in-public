(ns clojure-noob.ch5
  (:gen-class))


(println "Welcome to ch5 of Brave Clojure")

;; Pure Function

;; Referential Transparency

(+ 1 2)


(defn wisdom
  [words]
  (str words ", Daniel-san"))


(wisdom "Never clip your toes on a Tueday")
;; => "Never clip your toes on a Tueday, Daniel-san"

;; This function is not referentially transparent since it relies on a random number
(defn year-end-evaluation
  []
  (if (> (rand) 0.5)
    "You get a raise!"
    "Better luck next year :-("))


(year-end-evaluation)
;; => "Better luck next year :-("

;; => "You get a raise!"

;; => "Better luck next year :-("

;; If your function interacts with file systems, its not referentially transparent
(defn analysis
;; Referentially transparent

  "Returns the count of characters in given string"
  [text]
  (str "Character count: " (count text)))
;; 
(defn analyze-file
;; Not referentially transparent

  [filename]
  (analysis (slurp filename)))



;; Side FX
;; 
;; To perform a side effect is to change the association between a name and its value within a given scope.
;; 
;; Side effects allow you to interact with the real world
;; However, now you have to be careful about what the names in your code are referring to
;; 
;; Immutable Data Structures
;; Functional Alternative to Mutation is Recursion
;; 
(defn sum
  ; -> arity overloading to provide a default val of 0
  ([vals] (sum vals 0))
  ([vals accum]
   (if (empty? vals) ; -> recursion base case
     accum
     (sum (rest vals) (+ (first vals) accum)))))

(sum [39 5 1])
;; => 45

;; Each recursive call to sum creates a new scope where vals and accum are bound to different values, without needing to alter the originally passed values
;; 
(defn sum-recur
  ([vals]
   (sum vals 0))
  ([vals accum]
   (if (empty? vals)
     accum
     (recur (rest vals) (+ (first vals) accum)))))

(sum-recur [45 54 123])
;; => 222

;; Function Composition instead of Attribute Mutation
;; 
(require '[clojure.string :as s])
(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constrictor is so sassy lol!       ")
;; => "My boa constrictor is so sassy LOL!"

;; => "My boa constrictor is so sassy LOL!"

;; Functional Programming encourages you to build more complex functions by combining simpler functions.
;; 
;; Decoupling functions and data
;; Programming to a small set of abstractions
;; 
;; OOP -> modify data by embodying it as an object. original data is lost
;; FP -> Data is unchanging and you derive new data from existing data
;; 
;; comp
;; allows you to compose pure functions
;; 
((comp inc *) 2 3)
;; => 7

;; (comp f1 f2 f3) -> creates an anonymous function that composes the results of the args passed to it
;; 
;; (f1 (f2 (f3 x1 x2 x3)))
;; 
;; The innermost function (the last arg to comp) can take any number of args, but each of the successive functions must be able to take only one arg
;; 
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)
;; => 10

(c-str character)
;; => 4

(c-dex character)
;; => 5

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

(spell-slots character)

;; What do we do if one of the functions you want to compose needs to take more than one arg

(def spell-slots-comp (comp int inc #(/ % 2) c-int))
(spell-slots-comp character)

(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

((two-comp inc *) 2 3)
;; => 7

;; memoize
;; Memoization lets you take advantage of referential transparency by storing the arguments passed to a function and the return value of the function.
;; 
(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)

(sleepy-identity "Hello")
;; => "Hello" took 1 second
;; subsequent calls will also return "hello" after sleeping for 1 second

(def memo-sleepy-identity (memoize sleepy-identity))

(memo-sleepy-identity "Henlo Frens")
;; => "Henlo Frens" took 1 second

(memo-sleepy-identity "Henlo Frens")
;; => "Henlo Frens" returned immediately

(memo-sleepy-identity "Different arg")
;; => "Different arg" took 1 second

;; Peg Things
;; 
;; Reducing over functions is another way of composing functions
;; 
(defn clean-reduce
  [text]
  (reduce (fn [string string-fn] (string-fn string))
          text
          [s/trim #(s/replace % #"lol" "LOL")]))

;; 1. You used (comp :intelligence :attributes) to create a function that returns a character’s intelligence. Create a new function, attr, that you can call like (attr :intelligence) and that does the same thing.
;; 
(def attr #(get-in character [:attributes %]))

;; 2. Implement the comp function

;; comp function syntax -> (comp f g h ...)
;; comp takes a variable number of arguments
;; (f (g (h ...)))
;; 
(defn my-comp
  [& args]
  (fn [& more-args]
    (let [i (apply (last args) more-args)
          rest-args (reverse (drop-last 1 args))]
      (reduce (fn [acc v] (v acc)) i rest-args))))

;; 3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v]

(defn my-assoc-in
  [m [k & ks] v]
  (println m k ks v)
  (if (empty? ks)
    (assoc m k v)
    (assoc m k (my-assoc-in (get m k) ks v))))

;; 4. Look up and use the update-in function
(update-in {:hello {:si {:hi 4}}} [:hello :si :hi] + 20)
;; => {:hello {:si {:hi 24}}}

;; 5. Implement update-in
(defn my-update-in
  [m [k & ks] f & args]
  (println m k ks f args)
  (if (empty? ks)
    (assoc m k (apply f args))
    (assoc m k (my-update-in (get m k) ks f args))))

(update-in {:hello {:si {:hi 4}}} [:hello :si :hi] + 20 3333)
;; => {:hello {:si {:hi 3357}}}
