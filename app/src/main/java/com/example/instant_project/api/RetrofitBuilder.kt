package com.example.instant_project.api

import com.example.instant_project.model.CheckoutBackendApi
import com.example.instant_project.util.LiveDataCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private const val TIMEOUT_SECONDS = 15L
    private const val BASE_URL: String =  "https://stripe-mobile-payment-sheet-test-playground-v3.glitch.me/"
    private val logging = HttpLoggingInterceptor()


    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

   private val retrofitBuilder:  Retrofit.Builder by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(httpClient)
    }


    val apiService: CheckoutBackendApi by lazy{
        retrofitBuilder
            .build()
            .create(CheckoutBackendApi::class.java)
    }
}