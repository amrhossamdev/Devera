package com.devera.app.ui.profile.models.Body

data class UpdatePassword(
    val confirmPassword: String,
    val id: String,
    val password: String
)