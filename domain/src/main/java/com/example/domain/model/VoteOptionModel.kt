package com.example.domain.model

data class VoteOptionModel(
    val type: VoteType = VoteType.DATE,
    val option: String = "",
    val votedUserIds: List<String> = emptyList()
)