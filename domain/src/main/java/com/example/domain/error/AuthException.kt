package com.example.domain.error

sealed class AuthException: Exception() {
    object EmailFormatInvalid: AuthException()
    object PasswordTooShort : AuthException()
    object UserNotFound : AuthException()
    object WrongPassword : AuthException()
    data class Unknown(val msg: String?) : AuthException()
}
