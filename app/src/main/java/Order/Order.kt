package Order

import com.example.android.models.Product

data class Order(
    val orderId: String,
    val status: String,
    val products: List<Product>? = emptyList() // ✅ Avoids null issues
)

