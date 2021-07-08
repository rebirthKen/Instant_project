package com.example.instant_project.main

import com.example.instant_project.util.DataState

interface DataStateListener {
    fun onDataStateChange(dataState: DataState<*>?)
}