package com.devera.app.ui.profile.models

import java.io.Serializable

data class Post(
    val createdAt: String,
    val groupId: Int,
    val id: Int,
    val postAbstract: String,
    val postImage: Any,
    val updatedAt: String,
    val userId: Int,
    val userName: String
):Serializable