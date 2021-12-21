package com.devera.app.ui.home.models

data class VoteActionsModel(
    val downCounter: Int,
    val isDownVoted: Int,
    val isUpVoted: Int,
    val status: Boolean,
    val upCounter: Int
)