package com.devera.app.ui.home.models

import java.io.Serializable

data class CommentResponse(

    val comments: ArrayList<CommentModel>
) : Serializable