# LearnDataLogToday

## Chapter 1 - Basic Queries

1. Find the entity ids of movies made in 1987

```datalog
[:find ?e
 :where
 [?e :movie/year 1987]]
```

2. Find the entity-id and titles of movies in the database

```datalog
[:find ?e ?title
 :where
 [?e :movie/title ?title]]
```

3. Find the name of all people in the database

```datalog
[:find ?name
 :where
 [?e :person/name ?name]]
```

## Chapter 2 - Data Patterns

1. Find movie titles made in 1985

```datalog
[:find ?title
 :where
 [?m :movie/year 1985]
 [?m :movie/title ?title]]
```

2. What year was "Alien" released?

```datalog
[:find ?year
 :where
 [?m :movie/title "Alien"]
 [?m :movie/year ?year]]
```

3. Who directed RoboCop? You will need to use [<movie-eid> :movie/director <person-eid>] to find the director for a movie.

```datalog
[:find ?name
 :where
 [?m :movie/title "RoboCop"]
 [?m :movie/director ?p]
 [?p :person/name ?name]]
```

4. Find directors who have directed Arnold Schwarzenegger in a movie.

```datalog
[:find ?name
 :where
 [?p :person/name "Arnold Schwarzenegger"]
 [?m :movie/cast ?p]
 [?m :movie/director ?p1]
 [?p1 :person/name ?name]]
```

## Chapter 3 - Parametrized Queries

1. Find movie title by year

```datalog
[:find ?title
 :in $ ?year
 :where
 [?m :movie/year ?year]
 [?m :movie/title ?title]]
```

2. Given a list of movie titles, find the title and the year that movie was released.

```datalog
[:find ?title ?year
 :in $ [?title ...]
 :where
 [?m :movie/title ?title]
 [?m :movie/year ?year]]
```

3. Find all movie ?titles where the ?actor and the ?director has worked together

```datalog
[:find ?title
 :in $ ?actor ?director
 :where
 [?p :person/name ?director]
 [?m :movie/director ?p]
 [?m :movie/cast ?p1]
 [?p1 :person/name ?actor]
 [?m :movie/title ?title]]
```

4. Write a query that, given an actor name and a relation with movie-title/rating, finds the movie titles and corresponding rating for which that actor was a cast member.

```datalog
[:find ?title ?rating
 :in $ ?actor [[?title ?rating]]
 :where
 [?p :person/name ?actor]
 [?m :movie/cast ?p]
 [?m :movie/title ?title]]
```

## Chapter 4 - More queries

datom - `[eid attr val tx]`

Get the attribute keywords using the `:db/ident` attribute.

Only attribute associated with a transaction is `:db/txInstant` which is the instant in time when the tx was committed to the database.

1. What attributes are associated with a given movie.

```datalog
[:find ?attr
 :in $ ?title
 :where
 [?m :movie/title ?title]
 [?m ?a]
 [?a :db/ident ?attr]]
```

2. Find the names of all people associated with a particular movie (i.e. both the actors and the directors)

```datalog
[:find ?name
 :in $ ?title [?attr ...]
 :where
 [?m :movie/title ?title]
 [?m ?attr ?p]
 [?p :person/name ?name]]
```

3. Find all available attributes, their type and their cardinality. This is essentially a query to find the schema of the database. To find all installed attributes you must use the `:db.install/attribute attribute`. You will also need to use the `:db/valueType` and `:db/cardinality` attributes as well as :db/ident.

```datalog
[:find ?attr ?type ?card
 :where
 [_ :db.install/attribute ?a]
 [?a :db/valueType ?t]
g [?a :db/cardinality ?c]
 [?a :db/ident ?attr]
 [?t :db/ident ?type]
 [?c :db/ident ?card]]
```

4. When was the seed data imported into the database? Grab the transaction of any datom in the database, e.g., [_ :movie/title _ ?tx] and work from there.

```datalog
[:find ?inst
 :where
 [_ :movie/title _ ?tx]
 [?tx :db/txInstant ?inst]]
```

## Chapter 5 - Predicates

1. Find movies older than a certain year (inclusive)

```datalog
[:find ?title
 :in $ ?year
 :where
 [?m :movie/title ?title]
 [?m :movie/year ?y]
 [(<= ?y ?year)]]
```

2. Find actors older than Danny Glover

```datalog
[:find ?actor
 :where
 [?p1 :person/name "Danny Glover"]
 [?p2 :person/name ?actor]
 [?p1 :person/born ?b1]
 [?p2 :person/born ?b2]
 [_ :movie/cast ?p2]
 [(> ?b1 ?b2)]
 [?p2 :person/name ?actor]]
```

3. Find movies newer than ?year (inclusive) and has a ?rating higher than the one supplied

```datalog
[:find ?title
 :in $ ?year ?rating [[?title ?rating1]]
 :where
 [(< ?rating ?rating1)]
 [?m :movie/year ?y]
 [(<= ?year ?y)]
 [?m :movie/title ?title]]
```

## Chapter 6 - Transformation Functions

1. Find people by age. Use the function tutorial.fns/age to find the age given a birthday and a date representing "today".

```datalog
[:find ?name
 :in $ ?age ?today
 :where
 [?p :person/name ?name]
 [?p :person/born ?bday]
 [(tutorial.fns/age ?bday ?today) ?age]]
```

2. Find people younger than Bruce Willis and their ages.

```datalog
[:find ?name ?age
 :in $ ?today
 :where
 [?p1 :person/name "Bruce Willis"]
 [?p1 :person/born ?b1]
 [?p2 :person/name ?name]
 [?p2 :person/born ?b2]
 [(tutorial.fns/age ?b1 ?today) ?bruceage]
 [(tutorial.fns/age ?b2 ?today) ?age]
 [(< ?age ?bruceage)]
 [?p :person/name ?name]]
```

3. The birthday paradox states that in a room of 23 people there is a 50% chance that someone has the same birthday. Write a query to find who has the same birthday. Use the < predicate on the names to avoid duplicate answers. You can use (the deprecated) .getDate and .getMonth java Date methods.

```datalog
[:find ?name-1 ?name-2
 :where
 [?p1 :person/name ?name-1]
 [?p1 :person/born ?bday1]
 [?p2 :person/name ?name-2]
 [?p2 :person/born ?bday2]
 [(< ?name-1 ?name-2)]
 [(.getDate ?bday1) ?bdate1]
 [(.getMonth ?bday1) ?bm1]
 [(.getDate ?bday2) ?bdate2]
 [(.getMonth ?bday2) ?bm2]
 [(= ?bdate1 ?bdate2)]
 [(= ?bm1 ?bm2)]]
```

The given solution is - 

```datalog
[:find ?name-1 ?name-2
 :where
 [?p1 :person/name ?name-1]
 [?p2 :person/name ?name-2]
 [?p1 :person/born ?born-1]
 [?p2 :person/born ?born-2]
 [(.getMonth ?born-1) ?m]
 [(.getMonth ?born-2) ?m]
 [(.getDate ?born-1) ?d]
 [(.getDate ?born-2) ?d]
 [(< ?name-1 ?name-2)]]
```

The difference between this and my implementation is that the chapter solution uses the data pattern to match months and dates rather than an outright comparison.

## Chapter 7 - Aggregates

Aggregate functions like `sum` and `max` are written in the `:find` clause.

1. `count` th number of movies in the database

```datalog
[:find (count ?m)
 :where
 [?m :movie/title]]
```

2. Find the birth date of the oldest person in the database

```datalog
[:find (min ?date)
 :where
 [?p :person/born ?date]]
```

3. Given a collection of actors and (the now familiar) ratings data. Find the average rating for each actor. The query should return the actor name and the avg rating.

```datalog
[:find ?actor (avg ?rating)
 :in $ [?actor ...] [[?title ?rating]]
 :where
 [?m :movie/title ?title]
 [?m :movie/cast ?p]
 [?p :person/name ?actor]]
```
