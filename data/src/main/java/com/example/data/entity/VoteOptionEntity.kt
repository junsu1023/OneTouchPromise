package com.example.data.entity

import com.example.domain.model.VoteType

data class CreateVoteOptionEntity(
    val option: String = "",
    val type: VoteType,
    val votedUserIds: List<String> = emptyList()
)
