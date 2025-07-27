package com.example.onetouchpromise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserModel
import com.example.domain.status.FriendStatus
import com.example.domain.usecase.CheckFriendshipUseCase
import com.example.domain.usecase.GetFriendRequestUseCase
import com.example.domain.usecase.RespondToFriendRequestUseCase
import com.example.domain.usecase.SearchUserByEmailUseCase
import com.example.domain.usecase.SendFriendRequestUseCase
import com.example.onetouchpromise.contract.FriendRequestContract
import com.google.firebase.auth.FirebaseAuth
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
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val checkFriendshipUseCase: CheckFriendshipUseCase,
    private val respondToFriendRequestUseCase: RespondToFriendRequestUseCase,
    private val getFriendRequestUseCase: GetFriendRequestUseCase,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _searchUserByEmailResult = MutableStateFlow<UserModel?>(null)
    val searchUserByEmailResult: StateFlow<UserModel?> get() = _searchUserByEmailResult.asStateFlow()

    private val _friendRequestState = MutableStateFlow<FriendRequestContract>(FriendRequestContract.Idle)
    val friendRequestState: StateFlow<FriendRequestContract> get() = _friendRequestState.asStateFlow()

    private val _friendRequests = MutableStateFlow<List<UserModel>>(emptyList())
    val friendRequests: StateFlow<List<UserModel>> get() = _friendRequests.asStateFlow()

    init {
        fetchFriendRequests()
    }

    private fun fetchFriendRequests() {
        val myUid = auth.currentUser?.uid?: return
        getFriendRequestUseCase(myUid) { result ->
            _friendRequests.value = result
        }
    }

    fun onFriendRequestResponse(fromUid: String, accept: Boolean) {
        val myUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            respondToFriendRequestUseCase(myUid, fromUid, accept)
        }
    }

    fun searchUserByEmail(email: String) {
        viewModelScope.launch {
            val result = searchUserByEmailUseCase(email)
            _searchUserByEmailResult.update { result }
        }
    }

    fun onFriendRequestClick(friendUid: String) {
        viewModelScope.launch {
            val myUid = auth.currentUser?.uid ?: return@launch

            _friendRequestState.update { FriendRequestContract.Loading }

            when(val status = checkFriendshipUseCase(myUid, friendUid)) {
                FriendStatus.FRIENDS -> {
                    _friendRequestState.update { FriendRequestContract.AlreadyFriends }
                }
                FriendStatus.REQUEST_SENT -> {
                    _friendRequestState.update { FriendRequestContract.AlreadySent }
                }
                FriendStatus.REQUEST_RECEIVED -> {
                    _friendRequestState.update { FriendRequestContract.AlreadyReceived }
                }
                FriendStatus.NONE -> {
                    sendFriendRequestUseCase(myUid, friendUid)
                }
            }
        }
    }
}