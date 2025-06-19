package com.example.data.mapper

import com.example.data.entity.UserEntity
import com.example.domain.model.UserModel

fun UserModel.toEntity(): UserEntity = UserEntity(
    uid = this.id,
    email = this.email
)

fun UserEntity.toModel(): UserModel = UserModel(
    id = this.uid,
    email = this.email.orEmpty()
)