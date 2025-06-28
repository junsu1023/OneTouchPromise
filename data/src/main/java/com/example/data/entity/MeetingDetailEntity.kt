package com.example.data.entity

data class MeetingDetailEntity(
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val voteOptions: List<VoteOptionEntity> = emptyList(),
    val dueDate: String = ""
)