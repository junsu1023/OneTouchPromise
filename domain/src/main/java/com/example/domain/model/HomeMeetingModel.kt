package com.example.domain.model

data class HomeMeetingModel(
    val id: String = "",
    val title: String = "",
    val creatorEmail: String = "",
    val dueDate: String = "",
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val voteRatio: Float = 0f
)
