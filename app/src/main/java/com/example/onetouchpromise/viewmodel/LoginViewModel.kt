package com.example.onetouchpromise.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.error.AuthException
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.SaveFcmTokenUseCase
import com.example.onetouchpromise.contract.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase
): ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val result = loginUseCase(uiState.email, uiState.password)
            uiState = when {
                result.isSuccess -> uiState.copy(isLoading = false, isSuccess = true)
                else -> {
                    val exception = result.exceptionOrNull()
                    val authError = if(exception is AuthException) exception else AuthException.Unknown(exception?.message)
                    uiState.copy(error = authError, isLoading = false)
                }
            }
        }
    }

    fun saveFcmToken(token: String) {
        viewModelScope.launch {
            try {
                saveFcmTokenUseCase(token)
            } catch (e: Exception) {
                Log.d("FCM", "Error saving token: ${e.message}")
            }
        }
    }
}