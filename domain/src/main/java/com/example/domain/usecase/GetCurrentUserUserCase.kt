package com.example.domain.usecase

import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUserCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserModel? = authRepository.getCurrentUser()
}