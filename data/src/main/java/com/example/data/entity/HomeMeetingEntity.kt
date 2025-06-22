package com.example.data.entity

data class HomeMeetingEntity(
    val id: String = "",
    val title: String = "",
    val ownerId: String = "",
    val creatorEmail: String = "",
    val participants: List<String> = emptyList(),
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val dueDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
