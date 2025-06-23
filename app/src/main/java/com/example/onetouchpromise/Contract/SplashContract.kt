package com.example.onetouchpromise.Contract

sealed class SplashUiState {
    object Loading : SplashUiState()
    object NavigateToLogin : SplashUiState()
    object NavigateToHome : SplashUiState()
}