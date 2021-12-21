package com.devera.app.ui.groups.models.Body

data class AddPostBody(
    val groupId: Int,
    val postAbstract: String,
    val userId: Int,
)