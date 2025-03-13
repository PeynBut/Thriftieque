import api.OrderApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.184/android/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter for JSON
            .build()
    }

    // Instance for general ApiService (if needed)
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val orderInstance: OrderApiService by lazy {
        retrofit.create(OrderApiService::class.java) // Use OrderApiService here
    }
}
