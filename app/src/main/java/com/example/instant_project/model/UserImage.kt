package com.example.instant_project.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserImage(
    @Expose
    @SerializedName("email")
    val email: String? = null,

    @Expose
    @SerializedName("username")
    val username: String? = null,
    @Expose
    @SerializedName(value = "image")
    val image: String? = null
    ) {
    override fun toString(): String {
        return super.toString()
    }
}