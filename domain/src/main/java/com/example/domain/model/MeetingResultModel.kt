package com.example.domain.model

data class MeetingResultModel(
    val title: String = "",
    val dateResults: List<VoteResultItem> = emptyList(),
    val locationResults: List<VoteResultItem> = emptyList()
)

data class VoteResultItem(
    val option: String,
    val voteCount: Int,
    val voters: List<String>
)