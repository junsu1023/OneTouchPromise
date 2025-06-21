package com.example.domain.model

data class MeetingDetailModel(
    val id: String = "",
    val title: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val voteOptions: List<VoteOptionModel> = emptyList()
)

data class VoteOptionModel(
    val type: VoteType,
    val option: String,
    val votedUserIds: List<String>
)

enum class VoteType {
    DATE, LOCATION
}