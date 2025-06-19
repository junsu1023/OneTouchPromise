package com.example.domain.model

data class MeetingModel(
    val id: String = "",
    val title: String = "",
    val dueDate: String = "",
    val votedCount: Int = 0,
    val totalCount: Int = 0
)
