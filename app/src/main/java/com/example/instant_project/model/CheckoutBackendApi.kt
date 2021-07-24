package com.example.instant_project.model

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CheckoutBackendApi {

    @Headers("Content-Type: application/json")
    @POST("checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): CheckoutResponse
}