package com.example.data.entity

data class MeetingEntity(
    val id: String = "",
    val title: String = "",
    val dueDate: String = "",
    val votedCount: Int = 0,
    val totalCount: Int = 0
)