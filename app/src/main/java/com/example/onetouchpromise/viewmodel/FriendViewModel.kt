package com.example.onetouchpromise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserModel
import com.example.domain.usecase.SearchUserByEmailUseCase
import com.example.domain.usecase.SendFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val searchUserByEmailUseCase: SearchUserByEmailUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase
): ViewModel() {
    private val _searchUserByEmailResult = MutableStateFlow<UserModel?>(null)
    val searchUserByEmailResult: StateFlow<UserModel?> get() = _searchUserByEmailResult.asStateFlow()

    private val _sendFriendResult = MutableStateFlow<Result<Unit>?>(null)
    val sendFriendResult: StateFlow<Result<Unit>?> get() = _sendFriendResult.asStateFlow()

    fun searchUserByEmail(email: String) {
        viewModelScope.launch {
            val result = searchUserByEmailUseCase(email)
            _searchUserByEmailResult.update { result }
        }
    }

    fun sendFriendRequest(toUid: String) {
        viewModelScope.launch {
            val result = sendFriendRequestUseCase(toUid)
            _sendFriendResult.update { result }
        }
    }
}