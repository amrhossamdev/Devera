package com.devera.app.ui.home.models.BodyRequestsModel

data class AddCommentBody(
    val commentAbstract: String,
    val postId: Int,
    val userId: Int,
    val userName: String
)