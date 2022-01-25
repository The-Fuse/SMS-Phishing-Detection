package com.solvabit.phishingsmsdetector.api

import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


const val BASE_URL = "https://63f58654-5ce7-4243-874a-f8316aa48776.mock.pstmn.io/"

interface PhishingAPI {

    @Headers("Content-Type: application/json")
    @POST("post/")
    fun checkPhishing(@Body message: Phishing_Message): Call<Phishing>
}

object PhishingService {
    val phishingAPInstance : PhishingAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        phishingAPInstance = retrofit.create(PhishingAPI::class.java)
    }
}