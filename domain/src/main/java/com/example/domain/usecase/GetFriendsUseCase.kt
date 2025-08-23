package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        myUid: String,
        onResult: (List<String>) -> Unit
    ) = authRepository.getFriends(myUid, onResult)
}