package com.example.android.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
    val phone: String,  // ✅ Added phone number field
    val barangay: String,
    val municipality: String,
    val country: String,
    val province: String,
    @SerializedName("postal_code") val postalCode: String
)



// ✅ API Generic Response
data class ApiResponse(
    val error: Boolean = false,
    val message: String? = null,
    val product: Product? = null,
    val products: List<Product>? = null, // Ensure proper field usage with nullability
    val data: List<Product> // or List<Product_see> depending on your use case
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

@Parcelize
data class CartItem(
    val userId: Int,
    val productId: Int,
    val productName: String,
    val imageUrl: String, // Add image URL
    val quantity: Int
) : Parcelable


// Product Model
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String
) : Parcelable

data class Product_see(
    val id: Int,
    val name: String,
    val image: String,
    val price: Double
)
