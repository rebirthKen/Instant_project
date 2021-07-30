package com.example.instant_project.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.instant_project.repository.Repository
import com.example.instant_sample_sushi.databinding.ActivityMainBinding
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch

class MainActivity :  AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: PaymentSheetPlaygroundViewModel by viewModels {
        PaymentSheetPlaygroundViewModel.Factory(
            application
        )
    }

    private val customer: Repository.CheckoutCustomer = Repository.CheckoutCustomer.Returning

    private val googlePayConfig: PaymentSheet.GooglePayConfiguration? = null

    private val currency: Repository.CheckoutCurrency = Repository.CheckoutCurrency.EUR

    private val mode: Repository.CheckoutMode = Repository.CheckoutMode.Setup

    private lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)


        viewBinding.reloadButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.prepareCheckout(customer, currency, mode)
            }
        }


        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

    }

    private fun startCompleteCheckout() {
        val clientSecret = viewModel.clientSecret.value ?: return

        if (viewModel.checkoutMode == Repository.CheckoutMode.Setup) {
            paymentSheet.presentWithSetupIntent(
                clientSecret,
                makeConfiguration()
            )
        } else {
            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                makeConfiguration()
            )
        }
    }


    private fun makeConfiguration(): PaymentSheet.Configuration {
        return PaymentSheet.Configuration(
            merchantDisplayName = merchantName,
            customer = viewModel.customerConfig.value,
            googlePay = googlePayConfig
        )
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        viewModel.status.value = paymentResult.toString()
    }

    companion object {
        private const val merchantName = "Example, Inc."
    }
}
