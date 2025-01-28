import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("android/includes/v1/RegisterUsers.php")  // Endpoint for registration (adjust as needed)
    fun registerUser(
        @Body user: User  // Pass the User object as the body
    ): Call<ApiResponse>
}
