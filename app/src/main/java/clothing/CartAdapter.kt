package clothing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R
import kotlin.reflect.KFunction1

// In CartAdapter, ensure you're using the correct CartItem class
class CartAdapter(
    private val context: Context,
    private val itemList: List<CartItem>,  // Ensure this is CartItem from the correct package
    private val layoutType: Int,
    private val itemClickListener: (CartItem) -> Unit,  // Correct type for itemClickListener
    private val removeClickListener: (CartItem) -> Unit  // Correct type for removeClickListener
) : RecyclerView.Adapter<CartAdapter.ClothingViewHolder>() {
    // Your code remains the same



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_clothing, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        val item = itemList[position]
        holder.name.text = item.productId.toString() // Use productId from com.example.android.models.CartItem

        // Handle item click (open item details or navigate)
        holder.itemView.setOnClickListener {
            itemClickListener(item) // Trigger item click listener
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ClothingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.product_title)
        val price: TextView = itemView.findViewById(R.id.product_price)
        val image: ImageView = itemView.findViewById(R.id.product_image)
    }
}



