package com.example.domain.repository

import com.example.domain.model.UserModel

interface AuthRepository {
    suspend fun signUp(email: String, nickname: String, password: String): Result<UserModel>
    suspend fun login(email: String, password: String): Result<UserModel>
    suspend fun getCurrentUser(): UserModel?
    suspend fun getUserByEmail(email: String): Result<UserModel?>
    suspend fun updateNickname(newNickname: String): Result<Unit>
    suspend fun changePasswordWithReAuth(changePassword: String, newPassword: String): Result<Unit>
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmTokensByEmail(emails: List<String>): List<String>
    suspend fun searchUserByEmail(email: String): UserModel?
}