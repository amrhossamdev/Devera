package com.devera.app.ui.groups.models

import java.io.Serializable

data class Data(
    val createdAt: String,
    val groupName: String,
    val id: Int,
    val updatedAt: String
) : Serializable