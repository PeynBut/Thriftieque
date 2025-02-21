package com.rendonapp.thriftique

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("user_id") val userId: String, // User who is adding the item to the cart
    @SerializedName("product_id") val productId: Int // ID of the product being added
) : Parcelable {

    // Constructor to create the object from a Parcel
    constructor(parcel: Parcel) : this(
        userId = parcel.readString() ?: "",
        productId = parcel.readInt()
    )

    // Method to write the object to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeInt(productId)
    }

    // Method to describe the contents (not used here, so returns 0)
    override fun describeContents(): Int = 0

    // Companion object to recreate the object from a Parcel
    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
