package clothing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.R

class ClothingAdapter(private val itemList: List<ClothingItem>) :
    RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder>() {

    // ViewHolder class to hold the item views
    class ClothingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val titleView: TextView = view.findViewById(R.id.item_title)
        val descriptionView: TextView = view.findViewById(R.id.item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        // Inflate the item view for each grid item (your item_clothing.xml)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clothing, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        // Get the current item and bind the data to the views
        val currentItem = itemList[position]
        holder.imageView.setImageResource(currentItem.imageResId)
        holder.titleView.text = currentItem.title
        holder.descriptionView.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
