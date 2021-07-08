package com.example.instant_project.main.state

/***
 * this is the sec big property for MVI -> event driving nature
 * we are building a sealed class to set up different events which can be detected by the viewModel
 * and fire off to the ui
 */
sealed class MainStateEvent {
    class GetBlogPostsEvent: MainStateEvent()

    class GetUserEvent(val userId: String): MainStateEvent()

    class None: MainStateEvent()
}