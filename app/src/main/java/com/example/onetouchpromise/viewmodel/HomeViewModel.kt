package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ChangePasswordWithReAuthUseCase
import com.example.domain.usecase.GetCurrentUserUserCase
import com.example.domain.usecase.ObserveHomeMeetingsUseCase
import com.example.domain.usecase.UpdateNicknameUseCase
import com.example.onetouchpromise.contract.HomeUiState
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeHomeMeetingsUseCase: ObserveHomeMeetingsUseCase,
    private val getCurrentUserUserCase: GetCurrentUserUserCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val changePasswordWithReAuthUseCase: ChangePasswordWithReAuthUseCase
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private val _updateNicknameState = MutableStateFlow<Result<Unit>?>(null)
    val updateNicknameState: StateFlow<Result<Unit>?> get() = _updateNicknameState.asStateFlow()

    private val _changePasswordState = MutableStateFlow<Result<Unit>?>(null)
    val changePasswordState: StateFlow<Result<Unit>?> get() = _changePasswordState.asStateFlow()

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

    fun updateNickname(newNickname: String) {
        viewModelScope.launch {
            val updateResult = updateNicknameUseCase(newNickname)
            _updateNicknameState.update { updateResult }
        }
    }

    fun resetNickNameUpdateState() {
        _updateNicknameState.update { null }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            val changeResult = changePasswordWithReAuthUseCase(currentPassword, newPassword)
            _changePasswordState.update { changeResult }
        }
    }

    fun resetPasswordChangeState() {
        _changePasswordState.update { null }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}