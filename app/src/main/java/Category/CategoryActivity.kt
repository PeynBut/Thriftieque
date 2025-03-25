package Category

import Products.ProductAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.models.ApiResponse
import com.example.android.models.Product
import com.google.android.material.button.MaterialButton
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewCategorizedProduct: RecyclerView
    private var productList: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setupBackButton()
        recyclerViewCategorizedProduct = findViewById(R.id.recyclerViewCategorizedProduct)

        setupRecyclerView()
        setupCategoryButtons()

        // ✅ Initially fetch all products
        getProductsFromDatabaseOrAPI("All")
    }
    private fun getProductsFromDatabaseOrAPI(category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getProducts(category).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        println("Response: ${apiResponse}") // Debugging

                        if (apiResponse?.success == true && !apiResponse.products.isNullOrEmpty()) {
                            productList = apiResponse.products.filter { product ->
                                category == "All" || product.category == category
                            }
                            productAdapter.updateList(productList)
                        } else {
                            Toast.makeText(this@CategoryActivity, "No products found", Toast.LENGTH_SHORT).show()
                            productList = emptyList() // Clear the list if no products are found
                            productAdapter.updateList(productList)
                        }
                    } else {
                        Toast.makeText(this@CategoryActivity, "Response error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CategoryActivity, "Failed to load products: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun setupBackButton() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.categoryToolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(this, productList)
        recyclerViewCategorizedProduct.layoutManager = GridLayoutManager(this, 2) // Set to grid layout with 2 columns
        recyclerViewCategorizedProduct.setHasFixedSize(true) // Improves performance
        recyclerViewCategorizedProduct.adapter = productAdapter
    }



    private fun setupCategoryButtons() {
        val categoryButtons = mapOf(
            R.id.buttonAll to "All",
            R.id.buttonOldSchool to "Old School",
            R.id.buttonStreetWear to "Street Wear",
            R.id.buttonCasualFit to "Casual Fit"
        )

        categoryButtons.forEach { (buttonId, category) ->
            findViewById<MaterialButton>(buttonId).setOnClickListener {
                getProductsFromDatabaseOrAPI(category) // Fetch products from the backend based on category
                Toast.makeText(this, "$category Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
