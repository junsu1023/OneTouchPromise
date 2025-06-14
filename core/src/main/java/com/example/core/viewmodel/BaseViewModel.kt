package com.example.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object BaseViewModel: ViewModel() {
    private val _currentScreen = MutableSharedFlow<String>()
    val currentScreen: SharedFlow<String> get() = _currentScreen.asSharedFlow()
}