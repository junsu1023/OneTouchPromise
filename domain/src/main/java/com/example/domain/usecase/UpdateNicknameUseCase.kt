package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newNickname: String): Result<Unit> = authRepository.updateNickname(newNickname)
}