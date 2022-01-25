package com.solvabit.phishingsmsdetector.api

import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


const val BASE_URL = "https://.api/"

interface PhishingAPI {

    @Headers("Content-Type: application/json")
    @POST
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