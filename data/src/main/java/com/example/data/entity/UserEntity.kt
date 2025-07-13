package com.example.data.entity

data class UserEntity(
    val uid: String,
    val email: String?,
    val nickname: String?,
    val profileImageUrl: String? = null
)