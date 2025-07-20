package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(toUid: String): Result<Unit> {
        return authRepository.sendFriendRequest(toUid)
    }
}