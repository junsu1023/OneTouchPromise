package com.example.domain.model

data class VoteOptionModel(
    val type: String = "",
    val option: String = "",
    val votedUserIds: List<String> = emptyList()
)