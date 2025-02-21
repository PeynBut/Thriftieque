import com.example.android.models.ApiResponse
import com.example.android.models.LoginRequest
import com.example.android.models.LoginResponse
import com.example.android.models.Product
import com.example.android.models.RegisterPart1
import com.example.android.models.RegisterUserRequest
import com.rendonapp.thriftique.CartItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("includes/v1/RegisterPart1.php")
    fun registerUser(@Body registerRequest: RegisterPart1): Call<ApiResponse>

    @POST("includes/v1/userLogin.php")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("includes/v1/RegisterPart2.php")
    fun registerUserPart2(@Body registerRequest: RegisterUserRequest): Call<ApiResponse>

    @POST("includes/v1/Cart/cart.php")
    fun addToCart(@Body cartItem: CartItem): Call<ApiResponse>

    @POST("includes/v1/Products/products.php")
    fun createProduct(@Body product: Product?): Call<ApiResponse?>?

    @POST("includes/v1/Products/products.php")
    fun updateProduct(@Body product: Product?): Call<ApiResponse?>?

    @POST("includes/v1/Products/products.php")
    fun deleteProduct(@Body product: Product?): Call<ApiResponse?>?

    @GET("includes/v1/Products/products.php")
    fun getProductById(@Query("id") id: Int): Call<ApiResponse?>?

    @GET("includes/v1/Products/products.php")
    fun getProducts(): Call<ApiResponse?>?
}
