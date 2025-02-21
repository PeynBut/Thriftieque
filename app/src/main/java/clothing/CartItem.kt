package com.rendonapp.thriftique

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("user_id") val userId: Int, // User who is adding the item to the cart
    @SerializedName("product_id") val productId: Int, // ID of the product being added
    @SerializedName("quantity") val quantity: Int // Quantity of the product in the cart
) : Parcelable {
    constructor(parcel: Parcel) : this(
        userId = parcel.readInt(),
        productId = parcel.readInt(),
        quantity = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeInt(productId)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CartItem) return false
        return userId == other.userId && productId == other.productId && quantity == other.quantity
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + productId
        result = 31 * result + quantity
        return result
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
