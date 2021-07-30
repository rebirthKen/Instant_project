package com.example.instant_project.repository

import com.example.instant_project.model.CheckoutResponse

internal interface Repository {
    sealed class CheckoutCustomer(val value: String) {
        object Guest : CheckoutCustomer("guest")
        object New : CheckoutCustomer("new")
        object Returning : CheckoutCustomer("returning")
        data class WithId(val customerId: String) : CheckoutCustomer(customerId)
    }

    enum class CheckoutCurrency(val value: String) {
        USD("usd"),
        EUR("eur")
    }

    enum class CheckoutMode(val value: String) {
        Setup("setup"),
        Payment("payment")
    }

    suspend fun checkout(
        customer: CheckoutCustomer,
        currency: CheckoutCurrency,
        mode: CheckoutMode
    ): CheckoutResponse
}
