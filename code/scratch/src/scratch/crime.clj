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
       (into {})))

(defn fips-code
  "Converts county data into fips code"
  [county]
  (str (:fips_state_code county) (:fips_county_code county)))

;; Ch 07 Exercises

;; 1. most-duis tells us about the raw number of reports, but doesn’t account for differences in county population. One would naturally expect counties with more people to have more crime! Divide the :driving_under_influence of each county by its :county_population to find a prevalence of DUIs, and take the top ten counties based on prevalence. How should you handle counties with a population of zero?

(defn most-duis-per-capita
  "Given a JSON filename of UCR Crime data for a particular year, finds the counties with most DUIs weighted by Population"
  [file]
  (->> file
       load-json
       (sort-by :driving_under_influence)
       (remove (fn [county] (= (:county_population county) 0)))
       (map (fn [county]
              (let [dui  (:driving_under_influence county)
                    popl (:county_population county)
                    prev (* (float (/ dui popl)) 100)]
                {:code       (fips (fips-code county))
                 :dui        dui
                 :pop        popl
                 :rptcnt     (:coverage_indicator county)
                 :prevalence (str (format "%.2f" prev) "%")})))
       (sort-by :prevalence)
       (take-last 10)))

;; 3. We can generalize the most-duis function to handle any type of crime. Write a function most-prevalent which takes a file and a field name, like :arson, and finds the counties where that field is most often reported, per capita.

(defn most-prevalent
  "Given a JSON filename of UCR Crime data for a particular year, finds the counties with most crimes weighted by Population"
  [file crime top]
  (->> file
       load-json
       (remove (fn [county] (= (:county_population county) 0)))
       (map (fn [county]
              (let [crime_cnt (crime county)
                    popl      (:county_population county)
                    prev      (* (float (/ crime_cnt popl)) 100)]
                {:code       (fips (fips-code county))
                 :rawcrime   crime_cnt
                 :pop        popl
                 :prevalence (str (format "%.2f" prev) "%")})))
       (sort-by :prevalence)
       (take-last top)))
