package api


import android.content.Context
import com.example.android.models.Product
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Data Models for Order Requests and Responses
data class OrderRequest(
    val action: String = "create",
    val user_id: Int,
    val product_id: Int,
    val quantity: Int,
    val total_price: Double,
    val phone: String,
)


data class Order(
    val id: Int,
    val status: String?,
    val total_price: String?,
    val user_name: String?,
    val product_name: String?,
    val quantity: Int,
    @SerializedName("image") val image_url: String?
)



data class OrderResponse(
    val error: Boolean,
    val message: String,
    val orders: List<Order>
)

interface OrderApiService {
    @POST("Orders/Order.php")
    fun createOrder(@Body order: OrderRequest): Call<OrderResponse>

    @GET("Orders/Order.php")
    fun getOrders(@Query("user_id") userId: Int): Call<OrderResponse>
}

