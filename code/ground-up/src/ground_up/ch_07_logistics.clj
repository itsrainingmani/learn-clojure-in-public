(ns ground-up.ch-07-logistics
  (:require [ground-up.core]))

;; Every lein project starts out with a stub namespace containing a single function.

;; (ns scratch.core) -> ns is a macro that all the following code belongs to that namespace

;; def, defn etc always work in the scope of a particular namespace

;; fully qualified name for a function or var is ns/name.
;; For eg. scratch.core/foo

;; namespaces automatically include clojure.core so we can refer to all clojure.core functions and macros and special forms by their short names.

;; In the REPL, we can switch the namespace by running the (ns name) macro

;; To access something from a different namespace in the current one, we need to require it in the ns defn.
;; (ns user (:require [scratch.core])). Now functions in scratch.core can be accessed by their fully qualified name

;; We can alias a namespace
;; (ns user (:require [scratch.core :as c]))
;; scratch.core/foo becomes c/foo
;; Refer to a specific function or var from another namespace by it's short name
;; (ns user (:require [scratch.core :refer [foo]]))

;; Suck in every function from another namespace with :refer :all
;; (ns user (:require [scratch.core :refer :all]))

;; Using Namespaces allows us to control the complexity of our projects by isolating code into related and understandable pieces. 

;; Code and Tests

(defn pow
  "Raises base to the given power. (pow 3 2) returns 3^2: 9"
  [base power]
  (apply * (repeat base power)))
