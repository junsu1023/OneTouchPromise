package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCurrentUserUserCase
import com.example.domain.usecase.ObserveHomeMeetingsUseCase
import com.example.onetouchpromise.contract.HomeUiState
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeHomeMeetingsUseCase: ObserveHomeMeetingsUseCase,
    private val getCurrentUserUserCase: GetCurrentUserUserCase
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var listenerRegistration: ListenerRegistration? = null

    init {
        observeMeetings()
    }

    private fun observeMeetings() {
        uiState = uiState.copy(isLoading = true)

        listenerRegistration = observeHomeMeetingsUseCase { result ->
            uiState = when {
                result.isSuccess -> {
                    val meetings = result.getOrNull().orEmpty()
                    uiState.copy(
                        meetings = meetings,
                        isLoading = false,
                        error = null
                    )
                }
                else -> {
                    uiState.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "알 수 없는 오류 발생"
                    )
                }
            }
        }
    }

    fun getCurrentUSer() {
        viewModelScope.launch {
            val user = getCurrentUserUserCase()
            uiState = uiState.copy(currentUser = user)
        }
    }

    fun updateError(message: String) {
        uiState = uiState.copy(error = message)
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}