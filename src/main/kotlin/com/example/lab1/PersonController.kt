package com.example.lab1

import com.example.lab1.model.Person
import com.example.lab1.repository.PersonRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/persons")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun listPersons(): ResponseEntity<List<Person>> {
        val persons = personRepository.findAll()
        return ResponseEntity.ok(persons)
    }

    @PostMapping
    fun createPerson(@RequestBody request: Person): ResponseEntity<Person> {
        val newPerson = personRepository.save(request)
        return ResponseEntity(newPerson, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            ResponseEntity.ok(person.get())
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PatchMapping("/{id}")
    fun editPerson(@PathVariable id: Long, @RequestBody request: Person): ResponseEntity<Person> {
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            val updatedPerson = person.get().copy(
                name = request.name,
                age = request.age,
                address = request.address,
                work = request.work
            )
            personRepository.save(updatedPerson)
            ResponseEntity.ok(updatedPerson)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun deletePerson(@PathVariable id: Long): ResponseEntity<Unit> {
        return if (personRepository.existsById(id)) {
            personRepository.deleteById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
