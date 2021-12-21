package com.devera.app.ui.profile.models

import java.io.Serializable

data class User(
    val createdAt: String,
    val email: String,
    val fullName: String,
    val id: Int,
    val password: String,
    val updatedAt: String,
    val userName: String
):Serializable