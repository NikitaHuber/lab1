package bmstu.nzagainov.person.services

import bmstu.nzagainov.person.domain.Person
import bmstu.nzagainov.person.model.PersonRequest
import bmstu.nzagainov.person.model.PersonResponse
import bmstu.nzagainov.person.repository.PersonRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val personRepository: PersonRepository
) {

    @Transactional
    fun getPersons() = personRepository.findAll().map { it.toResponse() }

    @Transactional
    fun addPerson(request: PersonRequest) = personRepository.save(request.toEntity()).id

    @Transactional
    fun getPerson(id: Int) =
        personRepository
            .findById(id)
            .orElseThrow { EntityNotFoundException("Пользователь $id не найден") }
            .toResponse()

    @Transactional
    fun editPerson(id: Int, request: PersonRequest) = personRepository.save(
        personRepository
            .findById(id)
            .orElseThrow { EntityNotFoundException("Пользователь $id не найден") }
            .update(request)
    ).toResponse()

    @Transactional
    fun deletePerson(id: Int) {
        personRepository.deleteById(id)
    }

    private fun PersonRequest.toEntity() = Person(this.name, this.age, this.address, this.work)

    private fun Person.toResponse() = PersonResponse(this.id!!, this.name!!, this.age!!, this.address, this.work)

    private fun Person.update(request: PersonRequest): Person {
        name = request.name
        request.age?.let { this.age = it }
        request.address?.let { this.address = it }
        request.work?.let { this.work = it }

        return this
    }

}