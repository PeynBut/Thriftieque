import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val price: Double,

    @SerializedName("image") // ✅ Maps "image" from JSON to Kotlin
    val imageUrl: String
)

