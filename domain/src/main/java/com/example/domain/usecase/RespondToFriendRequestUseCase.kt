package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class RespondToFriendRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        myUid: String,
        fromUid: String,
        accept: Boolean
    ) {
        if(accept) authRepository.acceptFriendRequest(myUid, fromUid)
        else authRepository.declineFriendRequest(myUid, fromUid)
    }
}