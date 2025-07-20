package com.example.onetouchpromise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserModel
import com.example.domain.usecase.ChangePasswordWithReAuthUseCase
import com.example.domain.usecase.GetCurrentUserUserCase
import com.example.domain.usecase.UpdateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getCurrentUserUserCase: GetCurrentUserUserCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val changePasswordWithReAuthUseCase: ChangePasswordWithReAuthUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UserModel?>(null)
    val uiState: StateFlow<UserModel?> get() = _uiState.asStateFlow()

    private val _updateNicknameState = MutableStateFlow<Result<Unit>?>(null)
    val updateNicknameState: StateFlow<Result<Unit>?> get() = _updateNicknameState.asStateFlow()

    private val _changePasswordState = MutableStateFlow<Result<Unit>?>(null)
    val changePasswordState: StateFlow<Result<Unit>?> get() = _changePasswordState.asStateFlow()

    fun getCurrentUSer() {
        viewModelScope.launch {
            val user = getCurrentUserUserCase()
            _uiState.update { user }
        }
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
}