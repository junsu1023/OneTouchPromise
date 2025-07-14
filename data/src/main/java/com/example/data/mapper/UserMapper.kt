package com.example.data.mapper

import com.example.data.entity.UserEntity
import com.example.domain.model.UserModel

fun UserEntity.toModel(): UserModel = UserModel(
    id = this.uid,
    email = this.email.orEmpty(),
    nickname = this.nickname.orEmpty()
)