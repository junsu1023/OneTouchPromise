package com.example.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object BaseViewModel: ViewModel() {
    private val _currentScreen = MutableSharedFlow<String>()
    val currentScreen: SharedFlow<String> get() = _currentScreen.asSharedFlow()

    fun setCurrentScreen(screen: String) {
        viewModelScope.launch {
            _currentScreen.emit(screen)
        }
    }
}