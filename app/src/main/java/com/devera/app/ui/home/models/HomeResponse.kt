package com.devera.app.ui.home.models

import java.io.Serializable

data class HomeResponse(
    val `data`: Data,
    val status: Boolean
):Serializable