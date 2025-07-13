package com.example.onetouchpromise.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.domain.error.AuthException
import com.example.onetouchpromise.R

@Composable
fun getAuthError(exception: AuthException?): String? {
    return when(exception) {
        is AuthException.EmailFormatInvalid -> stringResource(R.string.invalid_email)
        is AuthException.NickNameTooShort -> stringResource(R.string.nickname_too_short)
        is AuthException.NickNameFormatInvalid -> stringResource(R.string.invalid_nickname)
        is AuthException.PasswordTooShort -> stringResource(R.string.password_too_short)
        is AuthException.UserNotFound -> stringResource(R.string.user_not_found)
        is AuthException.WrongPassword -> stringResource(R.string.wrong_password)
        is AuthException.Unknown -> exception.message ?: stringResource(R.string.unknown)
        null -> null
    }
}