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
