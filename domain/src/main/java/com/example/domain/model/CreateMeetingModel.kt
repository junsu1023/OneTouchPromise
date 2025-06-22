package com.example.domain.model

data class CreateMeetingModel(
    val title: String = "",
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val participants: List<String> = emptyList(),
    val creatorEmail: String = ""
)
