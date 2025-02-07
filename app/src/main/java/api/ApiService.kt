import com.example.android.models.ApiResponse
import com.example.android.models.LoginRequest
import com.example.android.models.LoginResponse
import com.example.android.models.RegisterPart1
import com.example.android.models.RegisterUserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("includes/v1/RegisterPart1.php")
    fun registerUser(@Body registerRequest: RegisterPart1): Call<ApiResponse>

    @POST("includes/v1/userLogin.php")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("includes/v1/RegisterPart2.php")
    fun registerUserPart2(@Body registerRequest: RegisterUserRequest): Call<ApiResponse>
}

