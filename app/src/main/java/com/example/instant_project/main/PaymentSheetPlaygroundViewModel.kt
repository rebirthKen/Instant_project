package com.example.instant_project.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.instant_project.api.RetrofitBuilder
import com.example.instant_project.repository.DefaultRepository
import com.example.instant_project.repository.Repository
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet

internal class PaymentSheetPlaygroundViewModel(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {
    val inProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()

    val customerConfig = MutableLiveData<PaymentSheet.CustomerConfiguration?>()
    val clientSecret = MutableLiveData<String?>()



    val readyToCheckout: LiveData<Boolean> = clientSecret.map {
        it != null
    }

    var checkoutMode: Repository.CheckoutMode? = null
    var temporaryCustomerId: String? = null

    /**
     * Calls the backend to prepare for checkout. The server creates a new Payment or Setup Intent
     * that will be confirmed on the client using Payment Sheet.
     */
    suspend fun prepareCheckout(
        customer: Repository.CheckoutCustomer,
        currency: Repository.CheckoutCurrency,
        mode: Repository.CheckoutMode
    ) {
        customerConfig.value = null
        clientSecret.value = null

        inProgress.postValue(true)

        runCatching {
            repository.checkout(customer, currency, mode)
        }.fold(
            onSuccess = {
                checkoutMode = mode
                temporaryCustomerId = if (customer == Repository.CheckoutCustomer.New) {
                    it.customerId
                } else {
                    null
                }

                // Init PaymentConfiguration with the publishable key returned from the backend,
                // which will be used on all Stripe API calls
                PaymentConfiguration.init(getApplication(), it.publishableKey)

                customerConfig.value = it.makeCustomerConfig()
                clientSecret.value = it.intentClientSecret
            },
            onFailure = {
                Log.e("qqqq", it.message.toString());
                status.postValue(
                    "Preparing checkout failed:\n${it.message}"
                )
            }
        )

        inProgress.postValue(false)
    }

    internal class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val checkoutBackendApi = RetrofitBuilder.apiService

            val repository = DefaultRepository(
                checkoutBackendApi
            )

            return PaymentSheetPlaygroundViewModel(
                application,
                repository
            ) as T
        }
    }
}