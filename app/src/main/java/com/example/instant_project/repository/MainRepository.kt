package com.example.instant_project.repository

import androidx.lifecycle.LiveData
import com.example.instant_project.api.RetrofitBuilder
import com.example.instant_project.model.Post
import com.example.instant_project.model.UserImage
import com.example.instant_project.main.state.MainViewState
import com.example.instant_project.util.ApiSuccessResponse
import com.example.instant_project.util.DataState
import com.example.instant_project.util.GenericApiResponse

object MainRepository {
    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return  object : NetworkBoundResource<List<Post>, MainViewState>() {
            override fun createCall(): LiveData<GenericApiResponse<List<Post>>> = RetrofitBuilder.apiService.getBlogPosts()

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<Post>>) {
                result.value = DataState.data(data = MainViewState(blogPosts = response.body) )
            }
        }.asLiveData()
    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return  object : NetworkBoundResource<UserImage, MainViewState>() {
            override fun createCall(): LiveData<GenericApiResponse<UserImage>> = RetrofitBuilder.apiService.getUser(userId)

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<UserImage>) {
                result.value = DataState.data(data = MainViewState(userImage = response.body) )
            }
        }.asLiveData()
    }
}
