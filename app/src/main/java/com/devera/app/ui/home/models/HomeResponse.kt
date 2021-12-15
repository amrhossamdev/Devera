package com.devera.app.ui.home.models

import java.io.Serializable

data class HomeResponse(

    val subjects: List<HomeModel>
) : Serializable