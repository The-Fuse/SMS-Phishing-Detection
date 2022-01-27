package com.solvabit.phishingsmsdetector

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
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

@BindingAdapter("checkPhishing")
fun bindSpamIndicator(layout: LinearLayout,message: Message?){
    message?.let {
        Log.i(TAG, "bindSpamIndicator: ${message.body} = safeScore = ${message.type}")
        val safeScore = message.creator
        when(safeScore) {
            in 0 .. 49 -> {
                layout.visibility = View.VISIBLE
                layout.setBackgroundResource(R.drawable.spam_bg)
            }
            in 50 .. 100 -> {
                layout.visibility = View.VISIBLE
                layout.setBackgroundResource(R.drawable.safe_bg)
            }
            else -> {
                layout.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("bindPhishingIcon")
fun bindFishingIcon(imageView: ImageView, message: Message?) {
    message?.let {
        val safeScore = message.creator
        when(safeScore) {
            in 0 .. 49 -> {
                imageView.setImageResource(R.drawable.ic_baseline_report_problem_24)
                imageView.visibility = View.VISIBLE
            }
            in 50 .. 100 -> {
                imageView.setImageResource(R.drawable.ic_baseline_insert_emoticon_24)
                imageView.visibility = View.VISIBLE
            }
            else -> {
                imageView.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("bindPhishingText")
fun bindFishingText(textView: TextView, message: Message?) {
    message?.let {
        val safeScore = message.creator
        when(safeScore) {
            in 0 .. 49 -> {
                textView.text = "This message looks suspicious"
                textView.visibility = View.VISIBLE
            }
            in 50 .. 100 -> {
                textView.text = "This message is Safe"
                textView.visibility = View.VISIBLE
            }
            else -> {
                textView.visibility = View.GONE
            }
        }
    }
}


@BindingAdapter("bindSenderImage")
fun bindSenderImage(imageView: ImageView, message: Message?) {
    message?.let {
        Log.i(TAG, "bindSenderImage: ${message.type}")
        when(message.type) {
            -1 -> {
                imageView.setImageResource(R.drawable.warning_icon)
                imageView.clearColorFilter()
            }
            else -> {
                imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                imageView.setColorFilter(getColor(message.address))
            }
        }

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

            }
            else -> {
                textView.setTextColor(Color.parseColor("#99000000"))

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
