package com.example.instant_project.main


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.example.instant_sample_sushi.R
import com.example.instant_project.util.DataState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), DataStateListener, PurchasesUpdatedListener {

    companion object {
        const val LICENCE = "type the licence here"
        const val PRODUCTION_ID = "sub_batch_membership_01"
    }

    lateinit var viewModel: MainViewModel

    private lateinit var billingClient: BillingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        viewModel = ViewModelProvider(this).get(MainViewModel :: class.java)
        showMainFragment()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    Log.e("qqqq", "connected ")
                    subscribe_text.text = subscribe_text.text.toString() + "\n connected!!"
                } else {
                    showToast("Something is wrong !!")
                }
            }
            override fun onBillingServiceDisconnected() {
                showToast("Disconnected!!")
            }
        })

        subscribe_button.setOnClickListener {
            subscribe()
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            subscribe_text.visibility = View.VISIBLE
            subscribe_text.text = "You have successfully bought the production"
        }
    }

    suspend fun initiatePurchase() {
        val skuList = ArrayList<String>()
        skuList.add(PRODUCTION_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        // leverage querySkuDetails Kotlin extension function
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }
       when(skuDetailsResult.billingResult.responseCode) {
           BillingClient.BillingResponseCode.OK -> {
               Log.e("qqqq", "billingResult OK")
               subscribe_text.text = subscribe_text.text.toString() + "\n billingResult OK"

               val skuDetailsList = skuDetailsResult.skuDetailsList
               if (!skuDetailsList.isNullOrEmpty()) {
                   val flowParams =  BillingFlowParams
                       .newBuilder()
                       .setSkuDetails(skuDetailsList[0])
                       .build()
                   billingClient.launchBillingFlow(this, flowParams)
               } else {
                   Log.e("qqqq", "skuDetailsList  null or empty")

               }

           }
           else -> {
               Log.e("qqqq", "billingResult Failure")
           }
       }

        // Process the result.
    }

    private fun initiatePurchaseTest() {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCTION_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        val billingResult = billingClient!!.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            billingClient!!.querySkuDetailsAsync(params.build()
            ) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (skuDetailsList != null && skuDetailsList.size > 0) {
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                        billingClient!!.launchBillingFlow(this@MainActivity, flowParams)
                    } else {
                        //try to add subscription item "sub_example" in google play console
                        Log.e("qqqq", "Item not Found -> " +  billingResult.debugMessage)

                        Toast.makeText(applicationContext, "Item not Found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("qqqq", "error -> " +  billingResult.debugMessage)
                    Toast.makeText(applicationContext,
                        " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(applicationContext,
                "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataState(dataState)
    }

    fun showMainFragment() {
        val fragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun handleDataState(dataState: DataState<*>?) {
        dataState?.let {
            //handle loading on ui
            showProgressBar(it.loading)

            //handle message on ui
            it.message?.let { event ->
                event.getContentIfNotHandled()?.let {
                    showToast(it)
                }
            }
        }
    }


    private fun subscribe() {
        if (billingClient.isReady) {
            //todo init the purchase
            Log.e("qqqq", "isReady")
            subscribe_text.text =   subscribe_text.text.toString() + "\n billingClient isReady"
            lifecycleScope.launchWhenStarted {
                initiatePurchaseTest()
            }
        } else {
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}