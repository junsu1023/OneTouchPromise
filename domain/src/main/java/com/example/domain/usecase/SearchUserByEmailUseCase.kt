package com.example.domain.usecase

import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class SearchUserByEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): UserModel? {
        return authRepository.searchUserByEmail(email)
    }
}