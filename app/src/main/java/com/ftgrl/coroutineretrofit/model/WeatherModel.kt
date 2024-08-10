package com.ftgrl.coroutineretrofit

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("name") val name: String,
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>



) {
    data class Main(
        @SerializedName("temp") val temp: Float,
        @SerializedName("temp_min") val tempMin: Float,
        @SerializedName("temp_max") val tempMax: Float
    )

    data class Weather(
        @SerializedName("description") val description: String
    )
}