package ru.netology.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.hibernate.entity.Person;
import ru.netology.hibernate.entity.PersonId;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий на нативных SQL-запросах в аннотации @Query (nativeQuery = true).
 * Основные CRUD-операции (save, findAll, findById, existsById, deleteById, ...) наследуются от JpaRepository.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    /** Все люди из указанного города (без учёта регистра). */
    @Query(value = "SELECT * FROM persons WHERE LOWER(city_of_living) = LOWER(:city)", nativeQuery = true)
    List<Person> findByCity(@Param("city") String city);

    /** Люди младше указанного возраста, по возрастанию возраста. */
    @Query(value = "SELECT * FROM persons WHERE age < :age ORDER BY age ASC", nativeQuery = true)
    List<Person> findYoungerThan(@Param("age") int age);

    /**
     * Человек с указанными именем и фамилией.
     * Ключ — name + surname + age, поэтому совпадений может быть несколько: LIMIT 1 берёт самого младшего.
     */
    @Query(value = "SELECT * FROM persons WHERE name = :name AND surname = :surname ORDER BY age ASC LIMIT 1",
            nativeQuery = true)
    Optional<Person> findByNameAndSurname(@Param("name") String name, @Param("surname") String surname);
}
