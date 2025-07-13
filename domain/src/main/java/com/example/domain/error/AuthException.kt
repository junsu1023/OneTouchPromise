package com.example.domain.error

sealed class AuthException: Exception() {
    object EmailFormatInvalid: AuthException()
    object NickNameTooShort: AuthException()
    object NickNameFormatInvalid: AuthException()
    object PasswordTooShort : AuthException()
    object UserNotFound : AuthException()
    object WrongPassword : AuthException()
    data class Unknown(val msg: String?) : AuthException()
}
