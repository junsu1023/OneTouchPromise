package com.example.domain.usecase

import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class GetFriendRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        myUid: String,
        onResult: (List<UserModel>) -> Unit
    ) = authRepository.getFriendRequests(myUid, onResult)
}