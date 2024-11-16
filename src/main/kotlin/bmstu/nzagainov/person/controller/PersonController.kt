package bmstu.nzagainov.person.controller

import bmstu.nzagainov.person.model.PersonRequest
import bmstu.nzagainov.person.services.PersonService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Int) = personService.getPerson(id)

    @GetMapping
    fun getPersons() = personService.getPersons()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addPerson(@RequestBody person: PersonRequest): String {
        val id = personService.addPerson(person)
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
            .toString()
    }

    @PatchMapping("/{id}")
    fun editPerson(@PathVariable id: Int, @RequestBody person: PersonRequest) = personService.editPerson(id, person)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePerson(@PathVariable id: Int) = personService.deletePerson(id)
}