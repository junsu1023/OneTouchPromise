package com.example.data.entity

data class CreateMeetingEntity(
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val voteOptions: List<CreateVoteOptionEntity> = emptyList()
)


