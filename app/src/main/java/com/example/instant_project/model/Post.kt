package com.example.instant_project.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(
    @Expose
    @SerializedName(value = "pk")
    val pk: Int? = null,

    @Expose
    @SerializedName(value = "title")
    val title: String? = null,

    @Expose
    @SerializedName(value = "body")
    val body: String? = null,

    @Expose
    @SerializedName(value = "image")
    val image: String? = null
) {
    override fun toString(): String {
        return super.toString()
    }
}