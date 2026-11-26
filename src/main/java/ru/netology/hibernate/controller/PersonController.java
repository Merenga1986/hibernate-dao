package ru.netology.hibernate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.hibernate.entity.Person;
import ru.netology.hibernate.entity.PersonId;
import ru.netology.hibernate.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // ---------- CRUD ----------

    /** GET /persons — все люди. */
    @GetMapping
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    /** GET /persons/one?name=Ivan&surname=Petrov&age=35 — один человек по первичному ключу. */
    @GetMapping("/one")
    public ResponseEntity<Person> getOne(@RequestParam("name") String name,
                                         @RequestParam("surname") String surname,
                                         @RequestParam("age") int age) {
        return ResponseEntity.of(personRepository.findById(new PersonId(name, surname, age)));
    }

    /** POST /persons — создать человека; 409, если такой ключ уже есть. */
    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (person.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (personRepository.existsById(person.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(personRepository.save(person));
    }

    /** PUT /persons — обновить телефон и город; 404, если человека нет. */
    @PutMapping
    public ResponseEntity<Person> update(@RequestBody Person person) {
        if (person.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!personRepository.existsById(person.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personRepository.save(person));
    }

    /** DELETE /persons?name=Ivan&surname=Petrov&age=35 */
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("name") String name,
                                       @RequestParam("surname") String surname,
                                       @RequestParam("age") int age) {
        PersonId id = new PersonId(name, surname, age);
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        personRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- запросы @Query (JPQL) ----------

    /** GET /persons/by-city?city=Moscow */
    @GetMapping("/by-city")
    public List<Person> getPersonsByCity(@RequestParam("city") String city) {
        return personRepository.findByCity(city);
    }

    /** GET /persons/by-age?age=30 — младше 30, по возрастанию возраста. */
    @GetMapping("/by-age")
    public List<Person> getPersonsYoungerThan(@RequestParam("age") int age) {
        return personRepository.findYoungerThan(age);
    }

    /** GET /persons/by-name-surname?name=Ivan&surname=Petrov — 404, если не найден. */
    @GetMapping("/by-name-surname")
    public ResponseEntity<Person> getPersonByNameAndSurname(@RequestParam("name") String name,
                                                            @RequestParam("surname") String surname) {
        return ResponseEntity.of(personRepository.findByNameAndSurname(name, surname));
    }
}
