package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SignUpUseCase
import com.example.onetouchpromise.Contract.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
): ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    fun signUp() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val result = signUpUseCase(uiState.email, uiState.password)
            uiState = when {
                result.isSuccess -> uiState.copy(isSuccess = true, isLoading = false)
                else -> uiState.copy(errorMessage = result.exceptionOrNull()?.message, isLoading = false)
            }
        }
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }
}