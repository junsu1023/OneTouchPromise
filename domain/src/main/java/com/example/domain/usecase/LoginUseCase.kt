package com.example.domain.usecase

import com.example.domain.error.AuthException
import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.util.isValidEmail
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserModel> {
        if(!email.isValidEmail()) return Result.failure(AuthException.EmailFormatInvalid)
        if(password.length < 6) return Result.failure(AuthException.PasswordTooShort)

        return try {
            authRepository.login(email, password)
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthException.UserNotFound
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw AuthException.WrongPassword
        } catch (e: Exception) {
            throw AuthException.Unknown(e.message)
        }
    }
}