package com.example.domain.usecase

import com.example.domain.error.AuthException
import com.example.domain.model.UserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.util.isNotValidEmail
import com.example.domain.util.isNotValidateNickname
import com.example.domain.util.isShorterThanLength2
import com.example.domain.util.isShorterThanLength8
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        nickname: String,
        password: String
    ): Result<UserModel> {
        if(email.isNotValidEmail()) return Result.failure(AuthException.EmailFormatInvalid)
        if(nickname.isShorterThanLength2()) return Result.failure(AuthException.NickNameTooShort)
        if(nickname.isNotValidateNickname()) return Result.failure(AuthException.NickNameFormatInvalid)
        if(password.isShorterThanLength8()) return Result.failure(AuthException.PasswordTooShort)

        return try {
            authRepository.signUp(email, nickname, password)
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthException.UserNotFound
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw AuthException.WrongPassword
        } catch (e: Exception) {
            throw AuthException.Unknown(e.message)
        }
    }
}