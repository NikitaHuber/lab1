package bmstu.nzagainov.person.repository

import bmstu.nzagainov.person.domain.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<Person, Int>