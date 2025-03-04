package Products

import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.android.models.Product
import com.rendonapp.thriftique.R

class ProductAdapter(private val context: Context, private var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val baseUrl = "http://192.168.100.184/thriftique_db/includes/v1/Products/uploads/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "$${product.price}"

        // Format Image URL
        val imageUrl = when {
            product.image.isNullOrEmpty() -> null  // Use default placeholder
            product.image.startsWith("http") -> product.image.trim()  // Already a full URL
            product.image.startsWith("uploads/") -> baseUrl + product.image.removePrefix("uploads/") // Prevent double "uploads/"
            else -> baseUrl + product.image.trim() // Standard case
        }

        // Load image using Glide
        Glide.with(context)
            .load(imageUrl ?: R.drawable.user) // Default image if null
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.blouse)  // Show while loading
                    .error(R.drawable.message)       // If load fails
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
            )
            .into(holder.ivProductImage)

        // Handle image click to open ProductDetailsActivity with vibration
        holder.ivProductImage.setOnClickListener {
            vibrate()
            val intent = Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra("product", product)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

    // Optimized data update using DiffUtil
    fun updateList(newList: List<Product>) {
        val diffCallback = ProductDiffCallback(productList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        productList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.productImage)
        val tvName: TextView = itemView.findViewById(R.id.productName)
        val tvPrice: TextView = itemView.findViewById(R.id.productPrice)
    }

    // DiffUtil class for efficient updates
    class ProductDiffCallback(private val oldList: List<Product>, private val newList: List<Product>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    // Function to trigger vibration
    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
