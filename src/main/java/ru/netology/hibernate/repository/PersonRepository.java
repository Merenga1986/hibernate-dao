package ru.netology.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.hibernate.entity.Person;
import ru.netology.hibernate.entity.PersonId;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий на методах-запросах Spring Data JPA.
 * Основные CRUD-операции (save, findAll, findById, existsById, deleteById, ...) наследуются от JpaRepository.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    /** Все люди из указанного города (без учёта регистра). */
    List<Person> findByCityOfLivingIgnoreCase(String city);

    /** Люди младше указанного возраста, по возрастанию возраста. */
    List<Person> findByIdAgeLessThanOrderByIdAgeAsc(int age);

    /**
     * Человек с указанными именем и фамилией.
     * Ключ — name + surname + age, поэтому совпадений может быть несколько: берётся первый (самый младший).
     */
    Optional<Person> findFirstByIdNameAndIdSurnameOrderByIdAgeAsc(String name, String surname);
}
