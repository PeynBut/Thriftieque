import com.google.gson.annotations.SerializedName

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)


// filepath: /app/src/main/java/com/example/android/ApiResponse.java
data class ApiResponse(
    val error: Boolean,
    val message: String?,
    val token: String?
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("token") val token: String?,
    @SerializedName("error") val error: Boolean? = false,
    @SerializedName("message") val message: String? = ""
)





