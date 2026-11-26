# Слой DAO c Hibernate

Домашнее задание «Слой DAO c Hibernate»: Spring Boot, `spring-boot-starter-data-jpa` + `spring-boot-starter-web`, PostgreSQL. Таблица `PERSONS` взята из задачи [«Таблица пользователей»](https://github.com/netology-code/jd-homeworks/blob/master/sql-basic/task/README.md).

## Запуск

```bash
docker compose up -d      # PostgreSQL на localhost:5432 (БД/пользователь/пароль: netology)
mvn spring-boot:run       # или запустить HibernateApplication из IDEA
```

Таблицу и тестовые данные создают миграции Flyway (`src/main/resources/db/migration`).

## API (ветка `jpa-repository-jpql-query`)

Репозиторий — интерфейс `PersonRepository extends JpaRepository<Person, PersonId>`: CRUD-методы наследуются, поисковые методы работают через **`@Query` с запросами на JPQL**.

| Запрос | Метод репозитория |
|---|---|
| `GET /persons` | `findAll()` |
| `GET /persons/one?name=Ivan&surname=Petrov&age=35` | `findById(PersonId)` |
| `POST /persons` (тело — Person) | `save()`; 409, если ключ занят |
| `PUT /persons` (тело — Person) | `save()`; 404, если человека нет |
| `DELETE /persons?name=Ivan&surname=Petrov&age=35` | `deleteById(PersonId)` |
| `GET /persons/by-city?city=Moscow` | `findByCity(city)`: `SELECT p FROM Person p WHERE LOWER(p.cityOfLiving) = LOWER(:city)` |
| `GET /persons/by-age?age=30` | `findYoungerThan(age)`: `... WHERE p.id.age < :age ORDER BY p.id.age ASC` |
| `GET /persons/by-name-surname?name=Ivan&surname=Petrov` | `findByNameAndSurname(name, surname)` → `Optional`: первый результат JPQL-запроса `... WHERE p.id.name = :name AND p.id.surname = :surname ORDER BY p.id.age`; 404, если нет |

Ключ таблицы — `name + surname + age`, поэтому людей с одинаковыми именем и фамилией может быть несколько (в тестовых данных — два Ivan Petrov). Метод по имени и фамилии возвращает `Optional` с первым из них (самым младшим).

Пример тела для `POST`/`PUT`:

```json
{"id": {"name": "Petr", "surname": "Volkov", "age": 40}, "phoneNumber": "+79990001122", "cityOfLiving": "Moscow"}
```

Примеры ответов на тестовых данных:
- `/persons/by-city?city=Moscow` → Ivan Petrov 35, Anna Ivanova 25, Pavel Kuznetsov 19
- `/persons/by-age?age=30` → Pavel 19, Ivan 20, Anna 25, Maria 28
- `/persons/by-name-surname?name=Ivan&surname=Petrov` → Ivan Petrov 20 (Kazan)

## Устройство

- `entity/Person`, `entity/PersonId`: Entity таблицы `persons`. Составной первичный ключ `name + surname + age` задан через `@EmbeddedId`.
- `repository/PersonRepository`: Spring Data JPA репозиторий с запросами JPQL в `@Query`.
- `controller/PersonController`: CRUD и поиск.
- `db/migration`: миграции Flyway, `V1` создаёт таблицу, `V2` добавляет тестовые данные.
- `docker-compose.yml`: PostgreSQL.

## Ветки

| Ветка | Задание |
|---|---|
| `main` | Слой DAO c Hibernate (`EntityManager`) |
| `jpa-repository` | Spring Data JPA: методы-запросы |
| `jpa-repository-jpql-query` | Spring Data JPA: `@Query` на JPQL |
| `jpa-repository-native-query` | Spring Data JPA: `@Query` с нативным SQL |
