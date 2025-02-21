package com.example.android.models

import com.google.gson.annotations.SerializedName

// ✅ Register Part 1 (Basic User Info)
data class RegisterPart1(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String  // Add this field
)

// ✅ Register Part 2 (Address & Location)
data class RegisterUserRequest(
    val barangay: String,
    val municipality: String,
    val country: String,
    val province: String,
    @SerializedName("postal_code") val postalCode: String, // Ensuring correct JSON field mapping
    @SerializedName("postalCode1") val postalCode1: String  // Corrected field name
)

// ✅ API Generic Response
data class ApiResponse(
    val error: Boolean = false,
    val message: String? = null,
    val product: Product? = null,
    val products: List<Product>? = null // Ensure proper field usage with nullability
)

// ✅ Login Request Model
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// ✅ Login Response Model
data class LoginResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("phone") val phoneNumber: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("message") val message: String? = ""
)

// Cart Item Model
data class CartItem(
    @SerializedName("user_id") val userId: String, // User who is adding the item to the cart
    @SerializedName("product_id") val productId: Int // ID of the product being added
)

// Product Model
data class Product(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val price: Double = 0.0
)
