package com.example.instant_project.api

import com.example.instant_project.model.CheckoutRequest
import com.example.instant_project.model.CheckoutResponse
import retrofit2.http.*


interface CheckoutBackendApi {
    @Headers("Content-Type: application/json")
    @POST("checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): CheckoutResponse
}