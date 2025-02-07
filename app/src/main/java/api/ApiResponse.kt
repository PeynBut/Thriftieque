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
data class RegisterUserRequest (
    val barangay: String,
    val municipality: String,
    val country: String,
    val province: String,
    @SerializedName("postal_code") val postalCode: String, // Ensuring correct JSON field mapping
    val postalCode1: String
)


// ✅ API Generic Response
data class ApiResponse(
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("token") val token: String? = null
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
