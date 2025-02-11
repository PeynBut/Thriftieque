package com.rendonapp.thriftique;

import ClothingItem
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import clothing.ClothingAdapter;
import clothing.ItemDetailActivity;
import kotlin.math.max;

class SeeAllItemsActivity : Activity() {
    private var recyclerView: RecyclerView? = null;
    private var clothingAdapter: ClothingAdapter? = null;
    private var itemList: List<ClothingItem>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_items);

        recyclerView = findViewById(R.id.recyclerViewAll);

        // Set GridLayoutManager based on screen width
        recyclerView?.layoutManager = GridLayoutManager(this, calculateNoOfColumns());

        // Initialize item list
        itemList = sampleClothingItems;

        // Set up adapter with click listener
        clothingAdapter = ClothingAdapter(this, itemList!!, 1) { item: ClothingItem ->
            this.onItemClicked(item);
        };
        recyclerView?.adapter = clothingAdapter;
    }

    // Function to calculate number of columns dynamically
    private fun calculateNoOfColumns(): Int {
        val displayMetrics = DisplayMetrics();
        windowManager.defaultDisplay.getMetrics(displayMetrics);
        val widthPixels = displayMetrics.widthPixels;
        val dpWidth = widthPixels / displayMetrics.density;
        val columnWidth = 180; // Approximate width of each column

        return max((dpWidth / columnWidth).toInt(), 2); // Ensure at least 2 columns
    }

    private val sampleClothingItems: List<ClothingItem>
        // Sample data for testing
        get() {
            val items: MutableList<ClothingItem> = ArrayList();
            items.add(ClothingItem(1, "T-Shirt", R.drawable.cool_tshirt, "Comfortable t-shirt", 19.99, 1));
            items.add(ClothingItem(2, "Jeans", R.drawable.skinny_jeans, "Stylish jeans", 39.99, 1));
            items.add(ClothingItem(3, "Jacket", R.drawable.long_sleeve, "Warm jacket", 59.99, 1));
            items.add(ClothingItem(4, "Dress", R.drawable.blouse, "Elegant dress", 29.99, 1));
            return items;
        }

    // Function triggered when an item is clicked
    private fun onItemClicked(item: ClothingItem) {
        vibrate(); // Vibrate on item click
        val intent = Intent(this, ItemDetailActivity::class.java);
        intent.putExtra("ITEM_DATA", item);
        startActivity(intent);
    }

    // Function for vibration feedback
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator;
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}