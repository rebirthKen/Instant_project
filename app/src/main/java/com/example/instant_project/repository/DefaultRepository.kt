package com.example.instant_project.repository

import com.example.instant_project.model.CheckoutBackendApi
import com.example.instant_project.model.CheckoutRequest

internal class DefaultRepository(
    private val checkoutBackendApi: CheckoutBackendApi
) : Repository {
    override suspend fun checkout(
        customer: Repository.CheckoutCustomer,
        currency: Repository.CheckoutCurrency,
        mode: Repository.CheckoutMode
    ) = checkoutBackendApi.checkout(CheckoutRequest(customer.value, currency.value, mode.value))
}
