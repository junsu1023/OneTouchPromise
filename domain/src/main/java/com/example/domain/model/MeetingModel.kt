package com.example.domain.model

data class MeetingModel(
    val id: String = "",
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val dueDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val alreadyVotes: List<String> = emptyList(),
    val voteRatio: Float,
    val isClosed: Boolean
)
