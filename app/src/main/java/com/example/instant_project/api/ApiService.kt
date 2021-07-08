package com.example.instant_project.api

import androidx.lifecycle.LiveData
import com.example.instant_project.model.Post
import com.example.instant_project.model.UserImage
import com.example.instant_project.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts(): LiveData<GenericApiResponse<List<Post>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ): LiveData<GenericApiResponse<UserImage>>
}