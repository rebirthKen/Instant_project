package com.example.instant_project.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.instant_project.util.*


abstract class NetworkBoundResource<ResponseObject, ViewStateType> {
    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)

        val apiResource = createCall()
        result.addSource(apiResource) { response ->
            result.removeSource(apiResource)

            handleNetworkCall(response)
        }
    }

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    fun handleNetworkCall(response: GenericApiResponse<ResponseObject>) {
        when(response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiErrorResponse -> {
                onReturnError(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                onReturnError("empty result happened")

            }
        }
    }

    fun onReturnError(message: String) {
        result.value = DataState.error(message)
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}