package Products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R

class CheckoutAdapter(
    private val context: Context,
    private val checkoutList: List<CartItem>
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = checkoutList[position]

        holder.tvProductName.text = item.productName
        holder.tvProductPrice.text = "₱${item.productPrice}"
        holder.tvProductQuantity.text = "Quantity: ${item.quantity}"

        Glide.with(context)
            .load(item.productImage)
            .placeholder(R.drawable.user) // Show while loading
            .error(R.drawable.user) // Use an existing image if loading fails
            .into(holder.ivProductImage)
    }

    override fun getItemCount(): Int = checkoutList.size

    class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivCheckout_ProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvCheckout_ProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvCheckout_Price)
        val tvProductQuantity: TextView = itemView.findViewById(R.id.tvCheckout_Quantity)
    }
}
