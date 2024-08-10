package com.ftgrl.coroutineretrofit

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ftgrl.coroutineretrofit.databinding.RowLayoutBinding

class RecyclerViewAdapter(
    private val weatherList: ArrayList<WeatherModel>,
    private val listener: Listener
) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(weatherModel: WeatherModel)
    }

    private val colors: Array<String> = arrayOf("#13bd27", "#29c1e1", "#b129e1", "#d3df13", "#f6bd0c", "#a1fb93", "#0d9de3", "#ffe48f")

    class RowHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val itemBinding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return weatherList.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        val weatherModel = weatherList[position]

        holder.itemView.setOnClickListener {
            listener.onItemClick(weatherModel)
        }
       /* holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))
        holder.binding.address.text = weatherModel.name
        holder.binding.updatedAt.text = "Temp: ${weatherModel.main.temp}°C, ${weatherModel.weather[0].description.capitalize()}"

        */
    }
}
