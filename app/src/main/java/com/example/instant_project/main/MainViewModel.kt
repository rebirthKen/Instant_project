package com.example.instant_project.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.instant_project.model.Post
import com.example.instant_project.model.UserImage
import com.example.instant_project.repository.MainRepository
import com.example.instant_project.main.state.MainStateEvent
import com.example.instant_project.main.state.MainViewState
import com.example.instant_project.util.AbsentLiveData
import com.example.instant_project.util.DataState

/***
 * generally in MVI, you only want to have two mutableLive data in your viewModel
 * one is for the stateEvent which gonna use as an intent for signifying
 * another is for the viewState which include all the data displaying on the ui
 */

class MainViewModel : ViewModel() {
    private val _viewStat: MutableLiveData<MainViewState> = MutableLiveData()

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()

    val viewState: LiveData<MainViewState>
    get() = _viewStat

    val dataState:  LiveData<DataState<MainViewState>> = Transformations.switchMap(_stateEvent) { stateEvent ->
        stateEvent?.let {
            handleStateEvent(stateEvent)
        }
    }

    private fun handleStateEvent(stateEvent: MainStateEvent):  LiveData<DataState<MainViewState>> = when(stateEvent) {
            is MainStateEvent.GetBlogPostsEvent -> {
               MainRepository.getBlogPosts()
            }
            is MainStateEvent.GetUserEvent -> {
                MainRepository.getUser(stateEvent.userId)
            }
            is MainStateEvent.None -> {
                AbsentLiveData.create()
            }
        }


    fun setUserImage(userImage: UserImage) {
        val update = getCurrentViewStateorNew()
        update.userImage = userImage
        _viewStat.value = update
    }

    fun setBlogListData(blogPosts: List<Post>) {
        val update = getCurrentViewStateorNew()
        update.blogPosts = blogPosts
        _viewStat.value = update
    }

    fun getCurrentViewStateorNew(): MainViewState {
        val value = viewState.value?.let {
            it
        } ?: MainViewState()

        return value
    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }
}
