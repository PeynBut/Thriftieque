package clothing





import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val cartList: MutableList<CartItem>,
    private val itemClickListener: (CartItem) -> Unit,
    private val removeClickListener: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]

        holder.tvProductName.text = cartItem.productName
        holder.tvProductPrice.text = "$${cartItem.productPrice}"
        holder.tvQuantity.text = "Qty: ${cartItem.quantity}"

        // Load the product image dynamically
        Glide.with(context)
            .load(cartItem.productImage)
            .placeholder(R.drawable.user) // Placeholder while loading
            .into(holder.ivProductImage)

        holder.itemView.setOnClickListener { itemClickListener(cartItem) }
        holder.btnRemove.setOnClickListener { removeClickListener(cartItem) }
    }

    override fun getItemCount(): Int = cartList.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
    }
}

