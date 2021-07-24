package com.example.instant_project.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val customer: String,
    val currency: String,
    val mode: String
)