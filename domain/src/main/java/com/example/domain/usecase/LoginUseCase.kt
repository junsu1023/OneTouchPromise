package com.example.domain.usecase

import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserModel> =
        authRepository.login(email, password)
}