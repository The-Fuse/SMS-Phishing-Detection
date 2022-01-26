package com.solvabit.phishingsmsdetector

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.solvabit.phishingsmsdetector.database.PhishedMessages
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.models.Thumbnails
import com.solvabit.phishingsmsdetector.screens.home.HomeAdapter
import java.util.*

@BindingAdapter("bindSenderData")
fun bindData(recyclerView: RecyclerView, data: List<Message>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindColor")
fun bindColor(imageView: ImageView, string: String?) {
    string?.let {
        imageView.setColorFilter(getColor(string))
    }
}

fun getColor(string: String): Int {
    val rnd = Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}

@BindingAdapter("bindTextColor")
fun bindTextColor(textView: TextView, int: Int?) {
    int?.let {
        Log.i(TAG, "bindTextColor: $it")
        when(it) {
            0 -> {
                textView.setTextColor(Color.parseColor("#000000"))
                textView.setTypeface(null, Typeface.BOLD)
            }
            else -> {
                textView.setTextColor(Color.parseColor("#99000000"))
            }
        }
    }
}

@BindingAdapter("setBarChart")
fun bindBarChart(barChart: BarChart, result: String) {
    val entries: ArrayList<BarEntry> = ArrayList()
    entries.add(BarEntry(1f, 4f))
    entries.add(BarEntry(2f, 10f))
    entries.add(BarEntry(3f, 2f))
    entries.add(BarEntry(4f, 15f))
    entries.add(BarEntry(5f, 13f))
    entries.add(BarEntry(6f, 2f))

    val barDataSet = BarDataSet(entries, "")
    barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

    val data = BarData(barDataSet)
    barChart.data = data


    //hide grid lines
    barChart.axisLeft.setDrawGridLines(false)
    barChart.xAxis.setDrawGridLines(false)
    barChart.xAxis.setDrawAxisLine(false)

    //remove right y-axis
    barChart.axisRight.isEnabled = false

    //remove legend
    barChart.legend.isEnabled = false


    //remove description label
    barChart.description.isEnabled = false


    //add animation
    barChart.animateY(3000)


    //draw chart
    barChart.invalidate()
}

@BindingAdapter("imageThumbnail")
fun bindImageThumbnail(imgView: ImageView, thumbnails: Thumbnails?) {
    thumbnails?.let {
        val imgUri = thumbnails.default.url.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

private const val TAG = "BindingAdapters"
