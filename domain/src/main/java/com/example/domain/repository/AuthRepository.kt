package com.example.domain.repository

import com.example.domain.model.UserModel

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<UserModel>
    suspend fun login(email: String, password: String): Result<UserModel>
    suspend fun getCurrentUser(): UserModel?
}