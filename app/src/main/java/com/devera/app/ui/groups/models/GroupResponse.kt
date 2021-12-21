package com.devera.app.ui.groups.models

import java.io.Serializable

data class GroupResponse(
    val `data`: List<Data>,
    val status: Boolean
):Serializable