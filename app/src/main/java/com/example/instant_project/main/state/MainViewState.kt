package com.example.instant_project.main.state

import com.example.instant_project.model.Post
import com.example.instant_project.model.UserImage

data class MainViewState(
    var blogPosts: List<Post>? = null,
    var userImage: UserImage? = null
)