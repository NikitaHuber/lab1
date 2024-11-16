package bmstu.nzagainov.person.model

data class PersonRequest(
    val name: String,
    val age: Int? = null,
    val address: String? = null,
    val work: String? = null
)