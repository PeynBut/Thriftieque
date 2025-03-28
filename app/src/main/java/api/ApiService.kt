import com.example.android.models.*
import com.rendonapp.thriftique.CartItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ✅ User Registration & Login
    @POST("register.php")
    fun signup(@Body request: SignUp): Call<SignUpResponse>

    @POST("includes/v1/RegisterPart1.php")
    fun registerUser(@Body registerRequest: RegisterPart1): Call<ApiResponse>

    @POST("userLogin.php")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("includes/v1/RegisterPart2.php")
    fun registerUserPart2(@Body registerRequest: RegisterUserRequest): Call<ApiResponse>

    @GET("verify_email.php")
    fun verifyEmail(@Query("email") email: String): Call<VerifyEmailResponse>

    @POST("reset_password.php")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<ResetPasswordResponse>



    // ✅ Cart Operations
    @POST("includes/v1/Cart/cart.php")
    fun addToCart(@Body cartItem: CartItem): Call<ApiResponse>

    @GET("get_cart.php")
    fun getCartItems(@Query("user_id") userId: Int): Call<List<CartItem>>

    // ✅ Product Management
    @POST("includes/v1/Products/products.php?action=create")
    fun createProduct(@Body product: Product): Call<ApiResponse>

    @POST("includes/v1/Products/products.php?action=update")
    fun updateProduct(@Body product: Product): Call<ApiResponse>

    @POST("includes/v1/Products/products.php?action=delete")
    fun deleteProduct(@Body product: Product): Call<ApiResponse>

    @GET("includes/v1/Products/products.php")
    fun getProductById(@Query("id") id: Int): Call<ApiResponse>

    @GET("Products/get_products.php")
    fun getProducts(@Query("category") category: String? = null): Call<ApiResponse>
}

