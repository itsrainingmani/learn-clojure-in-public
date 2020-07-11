(ns clojure-noob.ch6
  (:gen-class))

(map inc [1 2 3])
;; => (2 3 4)

inc
;; => #function[clojure.core/inc]

;; map and inc are symbols

;; When you give clojure a symbol like map,
;; it finds the corresponding var in the current namespace,
;; gets a shelf address, and retrieves an object from that
;; shelf for you.

;; if you want to use the symbol itself, quote it

'inc
;; => inc

'(map inc [1 2 3])
;; => (map inc [1 2 3])

(def great-books ["Sherlock Holmes" "Alice in Wonderland"])
;; => #'clojure-noob.ch6/great-books

great-books
;; => ["Sherlock Holmes" "Alice in Wonderland"]

;; Interning a var
;; Updates the current ns map with the association between great-books and the var

;; Stores the vector on a free shelf spot

;; Returns the address of the shelf on the var

;; Which is #'clojure-noob.ch6/great-books

;; use (ns-interns *ns*) to see the namespace's map

;; (get (ns-interns *ns*) 'great-books)
;; #'clojure-noob.ch6/great-books -> reader form of a var

;; use #' to grab ahold of the var corresponding to the symbol

(deref #'clojure-noob.ch6/great-books)
;; => ["Sherlock Holmes" "Alice in Wonderland"]

(def great-books ["The power of bees" "Journey to Upstairs"])

great-books
;; => ["The power of bees" "Journey to Upstairs"]

;; Var has been updated

;; create-ns -> take a symbol, creats a namepsace with that name if it doesnt exist already and returns the namespace

(create-ns 'cheese.taxonomy)
;; => #namespace[cheese.taxonomy]

;; You can use the returned namespace as an argument in a function call
(ns-name (create-ns 'cheese.taxonomy))
;; => cheese.taxonomy

;; in-ns -> creates a namespace and switches to it

;; use the fully-qualified name to refer to vars and fns from a different ns

;; refer -> fine grained control over how to refer objects in other namespaces
;; calling refer with a ns symbol lets you refer to the corresponding namespace's symbols without having to use fully-qualified symbols


;; filters -> :only, :exclude, :rename

;; private functions
;; defn-

;; cheese.analysis> (defn- priv-fn [])
;; #'cheese.analysis/priv-fn
;; cheese.analysis> (in-ns 'cheese.taxonomy)
;; #namespace[cheese.taxonomy]
;; cheese.taxonomy> (clojure.core/refer-clojure)
;; nil
;; cheese.taxonomy> (refer 'cheese.analysis :only ['priv-fn])
;; Execution error (IllegalAccessError) at cheese.taxonomy/eval8052 (form-init16120548889726586249.clj:7).
;; priv-fn is not public

;; Use @#'some/private-var to access the private function

;; alias lets you call functions in other namespaces with a shorter name (or more semantically relevant)

;; Project Organization

;; source code is in src/
;; Dashes in namespace names correspond to underscores in the filesystem

;; Requiring a namespace
;; To use code from a different file, we have to require it

;; require takes a symbol designating a namespace and ensures that the namespace exists and is ready to be used.
;; require lets you alias a namespace when you require it using :as or :alias

;; (require '[the-divine-cheese-code.visualization.svg :as svg])
;; equivalent to require and then alias

;; ns macro allows you to incorporate require, use, in-ns alias and refer
;; ns refers the clojure.core namespace by default

;; (ns the-divine-cheese-code.core
;;   (:refer-clojure :exclude [println]))

;; (:refer-clojure :exclude [println])
;; This is called a reference.

;; 6 possible kinds of references
;; (:refer-clojure)
;; (:require) -> works like the require function
;; (:use)
;; (:import)
;; (:load)
;; (:gen-class)

;; no need to quote symbols inside the ns macro
