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
 * Репозиторий на запросах JPQL в аннотации @Query.
 * Основные CRUD-операции (save, findAll, findById, existsById, deleteById, ...) наследуются от JpaRepository.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    /** Все люди из указанного города (без учёта регистра). */
    @Query("SELECT p FROM Person p WHERE LOWER(p.cityOfLiving) = LOWER(:city)")
    List<Person> findByCity(@Param("city") String city);

    /** Люди младше указанного возраста, по возрастанию возраста. */
    @Query("SELECT p FROM Person p WHERE p.id.age < :age ORDER BY p.id.age ASC")
    List<Person> findYoungerThan(@Param("age") int age);

    /** Все люди с указанными именем и фамилией, от младшего к старшему. */
    @Query("SELECT p FROM Person p WHERE p.id.name = :name AND p.id.surname = :surname ORDER BY p.id.age ASC")
    List<Person> findAllByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    /**
     * Человек с указанными именем и фамилией.
     * Ключ — name + surname + age, поэтому совпадений может быть несколько: берётся первый (самый младший).
     * В JPQL нет LIMIT, поэтому первый элемент выбирается из результата запроса выше.
     */
    default Optional<Person> findByNameAndSurname(String name, String surname) {
        return findAllByNameAndSurname(name, surname).stream().findFirst();
    }
}
