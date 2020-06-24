# Clojure Cheatsheet

## Types

- `"hello"` - string
- `\e` - character
- `0xBC` - hex
- `#"[0-9]+"` - regex
- `\newline` - special character
- `nil` - null val
- `true false` - bool
- `:alpha` - keyword
- `'(1 2 3)` - list
- `[1 2 3]` - vector
- `#{1 2 3}` - set
- `{:a 1, :b 2}` - map

## REPL commands

- `doc` - displays documentation for any function
- `apropos` - find functions that match a string or regexp
- `find-doc` - find functions where search term is in docstring
- `dir` - lists all functions in a namespace
- `source` - displays the underlying source of any function
- `type` - shows the type information for an object
- `meta` - displays the metadata associated with the object
- `supers` - lists all the supertypes for a given type. Useful when `type` doesn't have much detail

## Functions

- `#(+ 6 %)` - Anon func syntax
- `apply` - invokes a function 0 or more args
- `rand-int` - returns a rand int between 0 and arg
- `repeatedly` - takes a fn of no args with side effects and returns an infinite lazy sequence of calls to that fn
- `type` - returns `:type` metadata or its Class
- `inc` - increments the given number
- `dec` - decrements the given number
- `bigint` - converts number to an arbitrary-precision BigInt
- `int` - coerce to int
- `short` - coerce to short
- `byte` - coerce to byte
- `float` - coerce to float

* `+, -, *, /` - simple math operations
* `=` - Strict equality comparison
* `==` - Looser eq comparison. eg: `(== 3 3.0)` is `true` whereas `(= 3 3.0)` is `false`.

- `str` - with one arg, returns `x.toString()`. with multiple args, returns the concatenation of str values of the args
- `re-find` - if there is a regex match, return it. nil otherwise.
- `re-matches` - returns list of matches for your regex
- `boolean` - evaluates truthiness of the input. (`nil` and `false` are falsy values)
- `and` - returns the first negative value, or the last value if all are truthy.
- `or` - returns the first positive value
- `not` - inverts logical sense
- `nil?` - true if arg is nil, false otherwise

* `list` - List constructor
* `conj` - Adds the input to the front of the list. (Since lists are linked data structures). If the collection was a vector, `conj` would append to the end.
* `first` - gets the first element of a collection
* `second` - gets the second element of a collection
* `nth` - gets the nth element of a collection
* `vector` - creates a vector from multiple args
* `vec` - converts other structures into vectors
* `rest` - returns everything but the first element. `()` when there are no remaining elements.
* `next` - returns everything but the first element. `nil` when there are no remaining elements.
* `last` - last element of a collection
* `count` - number of elements in a collection

* `#{:a :b :c}` - Set constructor for a unique collection of values
* `sort` - asks for collection to be sorted in a specific order. Default sort order is ascending.
* `disj` - Remove element from a set. `(disj #{"hornet" "hummingbird"} "hummingbird")`
* `contains?` - boolean check if a collection contains an element
* `set` - make a set out of a different collection

* `{:a 1 :b 2 :c 3}` - Map constructor
* `get` - get value from a map given a key. nil if key doesn't exist. Can also give a default value if key isn't present.
* `get-in` - gets the value in a nested map given a sequence of keys.
* `:key` - use a keyword as a verb to look up vals in a map
* `assoc` - add a value for a given key to a map. If key is already present, `assoc` changes the value. `assoc` on nil creates a map.
* `merge` - combine maps. if same key is present in multiple maps, `merge` prefers values from later maps.
* `dissoc` - Remove a value from a map

* `resolve` - shows the intermediate value for a symbol
