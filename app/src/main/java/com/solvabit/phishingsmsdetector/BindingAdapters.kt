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
