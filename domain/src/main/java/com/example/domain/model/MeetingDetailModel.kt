package com.example.domain.model

data class MeetingDetailModel(
    val id: String = "",
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val voteOptions: List<VoteOptionModel> = emptyList(),
    val dueDate: String = "",
    val alreadyVotes: List<String> = emptyList()
)

enum class VoteType {
    DATE, LOCATION
}