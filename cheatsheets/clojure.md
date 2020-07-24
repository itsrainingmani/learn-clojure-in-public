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
- `type` - shows the type information for an object (`:type` metadata or it's Class)
- `meta` - displays the metadata associated with the object
- `supers` - lists all the supertypes for a given type. Useful when `type` doesn't have much detail

* `resolve` - shows the intermediate value for a symbol

## Functions

- `#(+ 6 %)` - Anon func syntax
- `apply` - invokes a function 0 or more args
- `rand-int` - returns a rand int between 0 and arg
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

### List functions

- `list` - List constructor
- `conj` - Adds the input to the front of the list. (Since lists are linked data structures). If the collection was a vector, `conj` would append to the end.
- `first` - gets the first element of a collection
- `second` - gets the second element of a collection
- `nth` - gets the nth element of a collection
- `vector` - creates a vector from multiple args
- `vec` - converts other structures into vectors
- `rest` - returns everything but the first element. `()` when there are no remaining elements.
- `next` - returns everything but the first element. `nil` when there are no remaining elements.
- `last` - last element of a collection
- `count` - number of elements in a collection
- `take` - selects first n elements
- `take-last` - selects last n elements
- `drop` - returns a list with the first n elements removed
- `drop-last` - returns a list with the last n elements removed
- `take-while` - like take but uses a predicate to decide when to stop
- `drop-while` - like drop but uses a predicate to decide when to stop

### Set Functions

- `#{:a :b :c}` - Set constructor for a unique collection of values
- `sort` - asks for collection to be sorted in a specific order. Default sort order is ascending.
- `disj` - Remove element from a set. `(disj #{"hornet" "hummingbird"} "hummingbird")`
- `contains?` - boolean check if a collection contains an element
- `set` - make a set out of a different collection
- `clojure.set/superset?` - Is set1 a superset of set2?
- `clojure.set/subset?` - Is set1 a subset of set2?

### Map Functions

- `{:a 1 :b 2 :c 3}` - Map constructor
- `get` - get value from a map given a key. nil if key doesn't exist. Can also give a default value if key isn't present.
- `get-in` - gets the value in a nested map given a sequence of keys.
- `:key` - use a keyword as a verb to look up vals in a map
- `assoc` - add a value for a given key to a map. If key is already present, `assoc` changes the value. `assoc` on nil creates a map.
- `merge` - combine maps. if same key is present in multiple maps, `merge` prefers values from later maps.
- `dissoc` - Remove a value from a map

### Sequence Functions

- `re-seq` - Returns a lazy sequence of successive matches of pattern in string.
- `apply` - Applies fn f to the argument list
- `->` - Threader macro form that inserts x as the second item in the first form (making it a list if it's not already) and so on.
- `->>` - Macro which inserts x as the last item in the first form and so on.
- `iterate` - Returns a lazy sequence of x, (f x), (f (f x)). f must be free of side effects
- `partition` - Returns a lazy sequence of lists of n items each at offsets step apart. If step isn't supplied, defaults to n.
- `partition-by` - Applies f to each val in coll, splitting it each time f returns a new value.
- `partition-all` - Like partition, but may include partitions with fewer than n items at the end
- `repeat` - Returns a lazy (inf or length n) sequence of xs
- `repeatedly` - takes a fn of no args with side effects and returns an infinite lazy sequence of calls to that fn
- `cycle` - Returns a lazy (inf) sequence of repetitions of the items in coll
- `map-indexed` - Like map, but the f should accept 2 args, index and item
- `concat` - Lazy seq of the concatenation of the elements in the supplied colls.
- `seq` - Returns a sequence on the collection. nil if coll is empty
- `shuffle` - Returns a random permutation of coll
- `interleave` - Returns a lazy-seq of the first item in each coll, then the second one
- `interpose` - Returns a lazy-seq of the elements of coll separated by sep
- `split-at` - vector of [(take n coll) (drop n coll)]
- `split-with` - vector of [(take-while pred coll) (drop-while pred coll)]
- `filter` - Returns lazy seq of items in coll for which (pred item) is logically true
- `remove` - Returns lazy seq of items in coll for which (pred item) is logically false
- `frequencies` - Returns map from distinct items in coll to the number of times they appear
- `group-by` - Returns a map of the elements of coll keyed by the result of f on each element. Value at each key will be a vector of the corresponding elements, in the order they appeared
- `reductions` - Returns a lazy seq of the intermediate values of the reduction of coll by f
- `into` - Returns a new coll consisting of to-coll with all of the items of from-coll conjoined
- `realized?` - Returns true if a value has been produced for a promise, delay, future or lazy seq
- `dotimes` - Repeatedly executes body (presumably for the side effect)
- `doseq` - Repeatedly executes body (presumably for side-effects) with
  bindings and filtering as provided by "for". Does not retain
  the head of the sequence. Returns nil.
- `doall` - When lazy sequences are produced via functions that have side
  effects, any effects other than those needed to produce the first
  element in the seq do not occur until the seq is consumed. doall can
  be used to force any effects. Walks through the successive nexts of
  the seq, retains the head and returns it, thus causing the entire
  seq to reside in memory at one time.
- `distinct` - returns a collection of all distinct elements
- `keys` - collection of keys in a map
- `vals` - collection of vals in a map
- `mapv` - like `map` but returns a vector. Very useful when passing to `transact!` which needs a vector input
- `clojure.string/escape` - Return a new string, using a map to escape each character ch from s
