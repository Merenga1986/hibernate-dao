package ru.netology.hibernate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.hibernate.entity.Person;

import java.util.List;

@Repository
public class PersonRepository {

    /** Spring внедряет потокобезопасный прокси EntityManager, привязанный к текущей транзакции. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Все люди из указанного города (без учёта регистра: Moscow, MOSCOW, moscow). */
    @Transactional(readOnly = true)
    public List<Person> getPersonsByCity(String city) {
        return entityManager
                .createQuery("SELECT p FROM Person p WHERE LOWER(p.cityOfLiving) = LOWER(:city)", Person.class)
                .setParameter("city", city)
                .getResultList();
    }
}
