(ns scratch.crime
  (:require [cheshire.core :as json]))

(defn load-json
  "Given a filename, reads a JSON file and returns it, parsed, with keywords"
  [file]
  (json/parse-string (slurp file) true)) ;; the true arg to parse-string coerces keys into keywords

(def fips
  "A Map of FIPS Codes to their country names"
  (->> "fips.json"
       load-json
       :table
       :rows
       (into {})))

(defn most-duis
  "Given a JSON filename of UCR Crime data for a particular year, finds the counties with most DUIs"
  [file]
  (->> file
       load-json
       (sort-by :driving_under_influence)
       (take-last 10)
       (map (fn [county]
              [(fips (fips-code county))
               (:driving_under_influence county)]))
       (into {})
       (sort-by key)))

(defn fips-code
  "Converts county data into fips code"
  [county]
  (str (:fips_state_code county) (:fips_county_code county)))
