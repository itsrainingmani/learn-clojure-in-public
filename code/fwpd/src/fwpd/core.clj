(ns fwpd.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;; Chapter Exercises
;; 1. Turn the result of your glitter filter into a list of names.

(defn glitter-filter-list
  [records]
  (map :name records))

;; 2. Write a function, append, which will append a new suspect to your list of suspects.

(defn append
  [suspect-list name glitter]
  (conj suspect-list {:name name, :glitter glitter}))

(def sus-list (mapify (parse (slurp filename))))
(append sus-list "Jim Moriarty" "300")

;; 3. Write a function, validate, which will check that :name and :glitter-index are present when you append. The validate function should accept two arguments: a map of keywords to validating functions, similar to conversions, and the record to be validated.

(defn validate
  [kw-validate-fn record]
  (apply = true (map (fn [k] (contains? (keys kw-validate-fn) (key k))) record)))

;; 4. Write a function that will take your list of maps and convert it back to a CSV string. You’ll need to use the clojure.string/join function.

(defn collapse-map
  [records]
  (clojure.string/join "\n" (map (fn [r] (clojure.string/join "," (map str (vals r)))) records)))
