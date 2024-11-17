package bmstu.nzagainov.person.services

import bmstu.nzagainov.person.domain.Person
import bmstu.nzagainov.person.model.PersonRequest
import bmstu.nzagainov.person.model.PersonResponse
import bmstu.nzagainov.person.repository.PersonRepository
import jakarta.persistence.EntityNotFoundException
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PersonServiceTest {

    val personRepository = mock(PersonRepository::class.java)
    val personService: PersonService = PersonService(personRepository)

    val person: Person by lazy { randomPerson(PERSON_ID) }

    @Test
    fun findAllWhenNoPersons() {
        `when`(personRepository.findAll()).thenReturn(listOf())

        assertThat(personService.getPersons()).isEmpty()
    }

    @Test
    fun findByIdWhenNoPerson() {
        `when`(personRepository.findById(PERSON_ID)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { personService.getPerson(PERSON_ID) }
    }

    @Test
    fun findByIdWhenPersonExists() {
        `when`(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(person))

        assertEquals(person, personService.getPerson(PERSON_ID).toEntity())
    }

    @Test
    fun createPersonWithValidData() {
        `when`(personRepository.save(any(Person::class.java))).thenReturn(person)

        assertEquals(PERSON_ID, personService.addPerson(person.toRequest()))
        verify(personRepository).save(person)
    }

    @Test
    fun editPersonWithValidData() {
        val editedPerson = person
        editedPerson.name = "Absolutely new name"

        `when`(personRepository.save(any(Person::class.java))).thenReturn(editedPerson)
        `when`(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(person))

        personService.editPerson(PERSON_ID, editedPerson.toRequest())
        assertEquals(editedPerson.toResponse(), personService.editPerson(PERSON_ID, editedPerson.toRequest()))
    }

    private fun randomPerson(id: Int) =
        Person(
            id = id,
            name = randomAlphabetic(8),
            address = randomAlphabetic(12),
            work = randomAlphabetic(8),
            age = nextInt(18, 60)
        )

    private fun Person.toResponse() = PersonResponse(this.id!!, this.name!!, this.age!!, this.address, this.work)
    private fun Person.toRequest() = PersonRequest(name!!, age, address, work)
    private fun PersonRequest.toEntity() = Person(this.name, this.age, this.address, this.work)
    private fun PersonResponse.toEntity() = Person(id, name, age, address, work)

    companion object {
        val PERSON_ID = 1
    }
}