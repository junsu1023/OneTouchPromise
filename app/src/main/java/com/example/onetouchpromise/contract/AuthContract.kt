package com.example.onetouchpromise.contract

import com.example.domain.error.AuthException

data class AuthUiState(
    val email: String = "",
    val nickname: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: AuthException? = null
)