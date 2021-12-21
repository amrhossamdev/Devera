package com.devera.app.ui.register.model

data class RegisterBody(
    val fullName: String,
    val userName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
