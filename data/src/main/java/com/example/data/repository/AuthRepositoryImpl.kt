package com.example.data.repository

import com.example.data.datasource.AuthDataSource
import com.example.data.mapper.toModel
import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
): AuthRepository {
    override suspend fun signUp(email: String, password: String): Result<UserModel> =
        authDataSource.signUp(email, password).map { it.toModel() }

    override suspend fun login(email: String, password: String): Result<UserModel> =
        authDataSource.login(email, password).map { it.toModel() }

    override suspend fun getCurrentUser(): UserModel? =
        authDataSource.getCurrentUser()?.toModel()
}