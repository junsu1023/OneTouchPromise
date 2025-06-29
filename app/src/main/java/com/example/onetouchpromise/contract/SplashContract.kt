package com.example.onetouchpromise.contract

sealed class SplashUiState {
    object Loading : SplashUiState()
    object NavigateToLogin : SplashUiState()
    object NavigateToHome : SplashUiState()
}