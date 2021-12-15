package com.devera.app.ui.home.models

data class HomeModel(
    val name: String,
    val desc: String,
    val personImage: String,
    val hasImage: Boolean,
    var reactId: Int?,

    )