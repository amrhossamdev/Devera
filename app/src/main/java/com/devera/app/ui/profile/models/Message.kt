package com.devera.app.ui.profile.models

import java.io.Serializable

data class Message(
    var downVoteCounter: Int,
    val post: Post,
    var upVoteCounter: Int,
    var userReact: Int
):Serializable