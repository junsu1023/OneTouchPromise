package com.example.data.repository

import com.example.data.datasource.AuthDataSource
import com.example.data.mapper.toModel
import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
): AuthRepository {
    override suspend fun signUp(email: String, nickname: String, password: String): Result<UserModel> =
        authDataSource.signUp(email, nickname, password).map { it.toModel() }

    override suspend fun login(email: String, password: String): Result<UserModel> =
        authDataSource.login(email, password).map { it.toModel() }

    override suspend fun getCurrentUser(): UserModel? =
        authDataSource.getCurrentUser()?.toModel()

    override suspend fun getUserByEmail(email: String): Result<UserModel?> =
        authDataSource.getUserByEmail(email).map { it?.toModel() }

    override suspend fun updateNickname(newNickname:String): Result<Unit> =
        authDataSource.updateNickname(newNickname)

    override suspend fun changePasswordWithReAuth(changePassword: String, newPassword: String): Result<Unit> =
        authDataSource.changePasswordWithReAuth(changePassword, newPassword)

    override suspend fun saveFcmToken(token: String) =
        authDataSource.saveFcmToken(token)

    override suspend fun getFcmTokensByEmail(emails: List<String>): List<String> =
        authDataSource.getTokensByEmail(emails)

    override suspend fun searchUserByEmail(email: String): UserModel? =
        authDataSource.searchUserByEmail(email)?.toModel()
}