package com.example.instant_project.util

data class DataState<T>(
    var message: Event<String>? = null,
    var loading: Boolean = false,
    var data: Event<T>? = null
) {
    companion object {
        fun <T> error(message: String): DataState<T> = DataState(message = Event(message), loading = false)

        fun <T> loading(isLoading: Boolean): DataState<T> = DataState(loading = isLoading)

        fun<T> data(message: String? = null, data: T? = null): DataState<T> = DataState(message = Event.messageEvent(message), data = Event.dataEvent(data))
    }
}