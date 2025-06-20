package com.example.data.entity

data class MeetingEntity(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val createdBy: String = "",
    val participants: List<String> = emptyList()
)