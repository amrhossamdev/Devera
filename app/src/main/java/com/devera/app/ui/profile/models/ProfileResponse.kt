package com.devera.app.ui.profile.models

import java.io.Serializable

data class ProfileResponse(
    val `data`: Data,
    val status: Boolean,
    val user: User
):Serializable