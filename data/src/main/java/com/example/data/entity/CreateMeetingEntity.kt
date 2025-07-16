package com.example.data.entity

data class CreateMeetingEntity(
    val id: String = "",
    val title: String = "",
    val ownerId: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val participantNicknames: Map<String, String> = emptyMap(),
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val voteOptions: List<VoteOptionEntity> = emptyList(),
    val dueDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val alreadyVotes: List<String> = emptyList()
)