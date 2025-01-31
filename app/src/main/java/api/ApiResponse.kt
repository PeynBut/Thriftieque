data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class ApiResponse(
    val error: Boolean,
    val message: String,
)