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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.models.Thumbnails
import com.solvabit.phishingsmsdetector.screens.home.HomeAdapter
import java.util.*
import kotlin.collections.ArrayList

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
        when(it) {
            0 -> {
                textView.setTextColor(Color.parseColor("#000000"))
                textView.setTypeface(null, Typeface.BOLD)
            }
            else -> {
                textView.setTextColor(Color.parseColor("#99000000"))
                textView.setTypeface(null, Typeface.NORMAL)
            }
        }
    }
}

@BindingAdapter("setBarChart")
fun bindBarChart(barChart: BarChart, score: Int) {
    val entries: ArrayList<BarEntry> = ArrayList()
    val x = score.toFloat()
    val y = 100-x
    entries.add(BarEntry(4f, y))
    entries.add(BarEntry(3f, x))

//    val labels = arrayOf( "Phished", "Safe")
//    val xAxis: XAxis = barChart.xAxis
//    xAxis.setCenterAxisLabels(false)
//    xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//    xAxis.axisMinimum = 0.01f
//    xAxis.valueFormatter = IndexAxisValueFormatter(labels)

    val barDataSet = BarDataSet(entries,"")
    barDataSet.setColors(*ColorTemplate.PASTEL_COLORS)

    val data = BarData(barDataSet)
    barChart.data = data

    barChart.axisLeft.axisMinimum = 0f

    //hide grid lines
    barChart.axisLeft.setDrawGridLines(false)
    barChart.axisLeft.setDrawAxisLine(false)
    barChart.xAxis.setDrawGridLines(false)
    barChart.xAxis.setDrawAxisLine(false)

    barChart.axisLeft.isEnabled = false
    barChart.xAxis.isEnabled = false
    //remove right y-axis
    barChart.axisRight.isEnabled = false

    //remove legend
    barChart.legend.isEnabled = false


    //remove description label
    barChart.description.isEnabled = false


    //add animation
   // barChart.animateY(3000)

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

@BindingAdapter("bindFormatDate")
fun bindDate(textView: TextView, date: Long?) {
    date?.let {
        val messageDate = Date(date).toString()
        val todayDate = Calendar.getInstance().time.toString()

        if(messageDate.substring(0, 10) == todayDate.substring(0, 10))
            textView.text = messageDate.substring(11, 16)
        else
            textView.text = messageDate.substring(4, 10)


    }
}

private const val TAG = "BindingAdapters"
