import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.Order
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rendonapp.thriftique.R

class OrderAdapter(private var orderList: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        // Log all data from the API response for debugging
        println("DEBUG: Order ID=${order.id}, Status=${order.status}, Price=${order.total_price}, Image=${order.image_url}")

        // Set order ID and status
        holder.tvOrderId.text = "Order ID: ${order.id ?: "N/A"}"
        holder.tvStatus.text = "Status: ${order.status ?: "Pending"}"

        // Handle price safely
        val price = order.total_price?.replace("₱", "")?.toDoubleOrNull() ?: 0.0
        holder.tvTotalPrice.text = "Total: ₱$price"

        // ✅ Define the image URL properly
        val imageUrl = order.image_url?.replace("http://http://", "http://") ?: ""

        Glide.with(holder.itemView.context)
            .load(if (imageUrl.isNotBlank()) imageUrl else R.drawable.shopping_cart) // Use placeholder if empty
            .placeholder(R.drawable.shopping_cart)
            .error(R.drawable.skinny_jeans) // Fallback if the image fails
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Optimize performance
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    println("DEBUG: Glide failed to load image -> ${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    println("DEBUG: Image loaded successfully: $imageUrl")
                    return false
                }
            })
            .into(holder.ivOrderImage)
    }

        override fun getItemCount(): Int = orderList.size

    // Update order list and notify adapter of the change
    fun updateOrders(newOrders: List<Order>) {
        if (orderList != newOrders) {
            orderList = newOrders
            notifyDataSetChanged()
        }
    }

    // ViewHolder to hold the references to the UI elements
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivOrderImage: ImageView = itemView.findViewById(R.id.orderImage)
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
    }
}
