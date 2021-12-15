package com.devera.app.ui.groups.models

import java.io.Serializable

data class GroupResponse(
    val groups: List<GroupModel>
) : Serializable