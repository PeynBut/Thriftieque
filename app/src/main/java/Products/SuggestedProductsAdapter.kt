package Products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.Constants
import com.bumptech.glide.Glide
import com.example.android.models.Product
import com.rendonapp.thriftique.R

class SuggestedProductsAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<SuggestedProductsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProductImage: ImageView = view.findViewById(R.id.ivSuggestedProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvSuggestedProductName)
        val tvProductPrice: TextView = view.findViewById(R.id.tvSuggestedProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_suggested_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position] // Move this to the top

        holder.tvProductName.text = product.name
        holder.tvProductPrice.text = "₱${product.price}"

        // Get base URL and construct full image URL
        val baseUrl = Constants.getBaseUrl(context)
        val imageUrl = when {
            product.image.isNullOrEmpty() -> null
            product.image.startsWith("http") -> product.image.trim() // Already a full URL
            product.image.startsWith("uploads/") -> baseUrl + product.image.removePrefix("uploads/")
            else -> baseUrl + product.image.trim() // Append base URL
        }

        // Debugging print statement to check final image URL
        println("Loading image: $imageUrl") // Check if URL is correct before loading in Glide

        // Load the product image with Glide
        Glide.with(context)
            .load(imageUrl ?: R.drawable.user)
            .placeholder(R.drawable.user) // Show placeholder while loading
            .error(R.drawable.blouse) // Show this if loading fails
            .into(holder.ivProductImage)

        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount(): Int = productList.size
}
