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


