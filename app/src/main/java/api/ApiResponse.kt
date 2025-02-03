package com.example.android.models

import com.google.gson.annotations.SerializedName

// User Registration Model (Handles address, coordinates, and phone number)
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    @SerializedName("phone") val phoneNumber: String,  // 👈 This ensures the correct field name
    val latitude: Double?,
    val longitude: Double?,
    val address: String
)


// API Generic Response
data class ApiResponse(
    @SerializedName("error") val error: Boolean = false, // Default to false
    @SerializedName("message") val message: String? = null,
    @SerializedName("token") val token: String? = null
)

// Login Request Model
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// Login Response Model (Includes phone number, address & coordinates)
data class LoginResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,  // ✅ Added Phone Number
    @SerializedName("latitude") val latitude: Double? = null,  // Optional
    @SerializedName("longitude") val longitude: Double? = null, // Optional
    @SerializedName("address") val address: String? = null, // Optional
    @SerializedName("error") val error: Boolean = false, // Default false to prevent null issues
    @SerializedName("message") val message: String? = ""
)
