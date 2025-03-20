package Order

data class Order(
    val name: String,  // ✅ Make sure it matches what your adapter is expecting
    val status: String
)