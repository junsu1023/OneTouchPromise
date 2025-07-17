package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordWithReAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> =
        authRepository.changePasswordWithReAuth(currentPassword, newPassword)
}