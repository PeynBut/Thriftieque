package api


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Data Models for Order Requests and Responses
data class OrderRequest(
    val action: String = "create",
    val user_id: Int,
    val product_id: Int,
    val quantity: Int,
    val total_price: Double,
    val phone: String
)

data class OrderResponse(
    val error: Boolean,
    val message: String
)

// Order API Interface
interface OrderApiService {
    @POST("includes/v1/Orders/Order.php")
    fun createOrder(@Body order: OrderRequest): Call<OrderResponse>
}
