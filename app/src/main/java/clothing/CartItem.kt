package com.rendonapp.thriftique

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val productName: String,
    val productImage: String, // Add this to store image URL
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
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
