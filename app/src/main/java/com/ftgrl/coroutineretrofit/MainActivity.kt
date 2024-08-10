package com.ftgrl.coroutineretrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftgrl.coroutineretrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var weatherModels: ArrayList<WeatherModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("MainActivity", "Error: ${throwable.localizedMessage}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.getWeatherData("Istanbul", "metric", "aee09b9ae9e7f40926afeeb6bb8522f5")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        Log.d("MainActivity", "Response: $weatherResponse")

                        // Update UI with API data
                        binding.address.text = weatherResponse.name

                        val unixTime = weatherResponse.dt  // `dt` değerini alın
                        val readableDate = convertUnixTimeToDateTime(unixTime)
                        Log.d("MainActivity", "Converted Date: $readableDate")

                        binding.updatedAt.text = "Updated at: ${readableDate}"



                        val temp = weatherResponse.main?.temp?.let { "${it}°C" } ?: "N/A"
                        binding.temp.text = temp

                        val minTemp = weatherResponse.main?.tempMin?.let { "Min Temp: ${it}°C" } ?: "N/A"
                        binding.tempMin.text = minTemp

                        val maxTemp = weatherResponse.main?.tempMax?.let { "Max Temp: ${it}°C" } ?: "N/A"
                        binding.tempMax.text = maxTemp

                        weatherModels = ArrayList(listOf(weatherResponse))
                        recyclerViewAdapter = RecyclerViewAdapter(weatherModels!!, this@MainActivity)
                        binding.recyclerView.adapter = recyclerViewAdapter
                    }
                } else {
                    Log.e("MainActivity", "Response Error: ${response.errorBody()}")
                }
            }
        }
    }

    override fun onItemClick(weatherModel: WeatherModel) {
        Toast.makeText(applicationContext, "Clicked on: ${weatherModel.name}", Toast.LENGTH_SHORT).show()
    }

    private fun convertUnixTimeToDateTime(unixTime: Long): String {
        val date = Date(unixTime * 1000)  // Unix zamanı saniye cinsindendir, milisaniyeye çeviriyoruz
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")  // İstediğiniz formatı belirleyin
        sdf.timeZone = TimeZone.getTimeZone("UTC")  // Zaman dilimini UTC olarak ayarlıyoruz
        return sdf.format(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
