(defproject ground-up "0.1.0-SNAPSHOT"
  :description "Project for Clojure from the Ground Up"
  :url "https://github.com/itsrainingmani/learn-clojure-in-public"
  :license {:name "MIT"
            :url  "https://mit-license.org"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot ground-up.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
