# Слой DAO c Hibernate

Домашнее задание «Слой DAO c Hibernate»: Spring Boot, `spring-boot-starter-data-jpa` + `spring-boot-starter-web`, PostgreSQL. Таблица `PERSONS` взята из задачи [«Таблица пользователей»](https://github.com/netology-code/jd-homeworks/blob/master/sql-basic/task/README.md).

## Запуск

```bash
docker compose up -d      # PostgreSQL на localhost:5432 (БД/пользователь/пароль: netology)
mvn spring-boot:run       # или запустить HibernateApplication из IDEA
```

Таблицу и тестовые данные создают миграции Flyway (`src/main/resources/db/migration`).

## Запрос

```
GET http://localhost:8080/persons/by-city?city=Moscow
```

```json
[
  {"id": {"name": "Ivan", "surname": "Petrov", "age": 35}, "phoneNumber": "+79161234567", "cityOfLiving": "Moscow"},
  {"id": {"name": "Anna", "surname": "Ivanova", "age": 25}, "phoneNumber": "+79162345678", "cityOfLiving": "Moscow"},
  {"id": {"name": "Pavel", "surname": "Kuznetsov", "age": 19}, "phoneNumber": "+79055678901", "cityOfLiving": "Moscow"}
]
```

Город сравнивается без учёта регистра.

## Устройство

- `entity/Person`, `entity/PersonId`: Entity таблицы `persons`. Составной первичный ключ `name + surname + age` задан через `@EmbeddedId`.
- `repository/PersonRepository`: `@Repository`. `EntityManager` внедряется через `@PersistenceContext`, `getPersonsByCity(String city)` выполняет JPQL-запрос.
- `controller/PersonController`: `GET /persons/by-city?city=...`.

## Ветки

| Ветка | Задание |
|---|---|
| `main` | Слой DAO c Hibernate (`EntityManager`) |
| `jpa-repository` | Spring Data JPA: методы-запросы |
| `jpa-repository-jpql-query` | Spring Data JPA: `@Query` на JPQL |
| `jpa-repository-native-query` | Spring Data JPA: `@Query` с нативным SQL |
