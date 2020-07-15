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

