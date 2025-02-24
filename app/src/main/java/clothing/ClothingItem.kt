import android.os.Parcel
import android.os.Parcelable

data class ClothingItem(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val imageResId: Int?, // Nullable for resource-based images
    val imageUrl: String?  // Nullable for URL-based images
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "", // Read non-null String
        parcel.readString() ?: "", // Read non-null String
        parcel.readDouble(),
        parcel.readValue(Int::class.java.classLoader) as? Int, // Read nullable Int
        parcel.readString() // Read nullable String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeValue(imageResId) // Write nullable Int
        parcel.writeString(imageUrl)  // Write nullable String
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
