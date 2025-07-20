package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.status.FriendStatus
import javax.inject.Inject

class CheckFriendshipUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(fromUid: String, toUid: String): FriendStatus = authRepository.checkFriendShipStatus(fromUid, toUid)
}