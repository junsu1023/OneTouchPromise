package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.onetouchpromise.Contract.SplashUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {
    var uiState by mutableStateOf<SplashUiState>(SplashUiState.Loading)
        private set

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val currentUser = auth.currentUser

        if(currentUser != null) {
            uiState = SplashUiState.NavigateToHome
        } else {
            uiState = SplashUiState.NavigateToLogin
        }
    }
}