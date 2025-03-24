package com.example.android.models


import android.os.Parcel
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName



data class SignUp(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
data class SignUpResponse(
    val message: String?,
    val error: Boolean?,  // Change from String? to Boolean?
    val token: String?
)


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
    val success: Boolean,
    val status: String
)

// ✅ Login Request Model
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    val id: Int,
    val name: String?,  // Ensure this exists
    val email: String?,
    val token: String?
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


@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val quantity: Int = 1,
    val category: String,
    val stock: Int
) : Parcelable


data class Product_see(
    val id: Int,
    val name: String,
    val image: String,
    val price: Double
)
