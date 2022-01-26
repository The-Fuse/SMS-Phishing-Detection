package com.solvabit.phishingsmsdetector.api

import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import com.solvabit.phishingsmsdetector.models.YoutubeData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


const val BASE_URL = "https://63f58654-5ce7-4243-874a-f8316aa48776.mock.pstmn.io/"
const val yt_developer_key = "AIzaSyC30WdEEv2azB9iIDGZbTUKQ9V6_VR9jmc"

interface PhishingAPI {

    @Headers("Content-Type: application/json")
    @POST("post/")
    fun checkPhishing(@Body message: Phishing_Message): Call<Phishing>

    @GET("search")
    fun getYoutubeVideos(
        @Query("q") q: String,
        @Query("key") developerKey: String = yt_developer_key,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video"
    ): Call<YoutubeData>
}

object PhishingService {
    val phishingAPInstance: PhishingAPI
    val youtubeAPInstance: PhishingAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitYoutube = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        phishingAPInstance = retrofit.create(PhishingAPI::class.java)
        youtubeAPInstance = retrofitYoutube.create(PhishingAPI::class.java)
    }
}