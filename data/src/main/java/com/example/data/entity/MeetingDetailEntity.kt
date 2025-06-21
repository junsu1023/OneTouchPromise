package com.example.data.entity

data class MeetingDetailEntity(
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val voteOptions: List<VoteOptionEntity> = emptyList()
)

data class VoteOptionEntity(
    val type: String = "",
    val option: String = "",
    val votedUserIds: List<String> = emptyList()
)