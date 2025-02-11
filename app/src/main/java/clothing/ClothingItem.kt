import android.os.Parcel
import android.os.Parcelable

data class ClothingItem(
    val id: Int, // Ensure this field exists
    val title: String,
    val imageResId: Int,
    val description: String,
    val price: Double,
    val categoryId: Int


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(imageResId)
        parcel.writeString(description)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ClothingItem> {
        override fun createFromParcel(parcel: Parcel): ClothingItem {
            return ClothingItem(parcel)
        }

        override fun newArray(size: Int): Array<ClothingItem?> {
            return arrayOfNulls(size)
        }
    }
}
