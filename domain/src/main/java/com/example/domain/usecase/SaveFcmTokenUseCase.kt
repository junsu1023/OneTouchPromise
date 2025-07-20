package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String) = authRepository.saveFcmToken(token)
}