package com.solvabit.phishingsmsdetector

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.phishingsmsdetector.models.Message
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

private const val TAG = "BindingAdapters"
