package com.example.android.models

import android.os.Parcel
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
    val data: List<Product>?, // or List<Product_see> depending on your use case
    val success: Boolean
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


data class CartItem(
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val productName: String,
    val productImage: String, // Ensure this holds a proper URL
    val productPrice: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeInt(productId)
        parcel.writeInt(quantity)
        parcel.writeString(productName)
        parcel.writeString(productImage)
        parcel.writeDouble(productPrice)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel) = CartItem(parcel)
        override fun newArray(size: Int) = arrayOfNulls<CartItem?>(size)
    }
}


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
