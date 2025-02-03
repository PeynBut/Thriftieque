import com.example.android.models.ApiResponse
import com.example.android.models.LoginRequest
import com.example.android.models.LoginResponse
import com.example.android.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("includes/v1/RegisterUsers.php")
    fun registerUser(@Body registerRequest: User): Call<ApiResponse>
    @POST("includes/v1/userLogin.php")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}
