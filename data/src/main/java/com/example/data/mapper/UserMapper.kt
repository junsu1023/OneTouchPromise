package com.example.data.mapper

import com.example.data.entity.UserEntity
import com.example.domain.model.UserModel

fun UserEntity.toModel(): UserModel = UserModel(
    id = uid,
    email = email,
    nickname = nickname
)