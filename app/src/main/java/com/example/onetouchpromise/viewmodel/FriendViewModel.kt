package com.example.onetouchpromise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserModel
import com.example.domain.status.FriendStatus
import com.example.domain.usecase.CheckFriendshipUseCase
import com.example.domain.usecase.GetFriendRequestUseCase
import com.example.domain.usecase.GetFriendsUseCase
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
    private val getFriendsUseCase: GetFriendsUseCase,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _searchUserByEmailResult = MutableStateFlow<UserModel?>(null)
    val searchUserByEmailResult: StateFlow<UserModel?> get() = _searchUserByEmailResult.asStateFlow()

    private val _friendRequestState = MutableStateFlow<FriendRequestContract>(FriendRequestContract.Idle)
    val friendRequestState: StateFlow<FriendRequestContract> get() = _friendRequestState.asStateFlow()

    private val _friendList = MutableStateFlow<List<String>>(emptyList())
    val friendList: StateFlow<List<String>> get() = _friendList.asStateFlow()

    init {
        getFriends()
    }

    private fun getFriends() {
        val myUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            getFriendsUseCase(myUid) { result ->
                _friendList.value = result
            }
        }
    }

    fun searchUserByEmail(email: String) {
        viewModelScope.launch {
            val result = searchUserByEmailUseCase(email)
            _searchUserByEmailResult.update { result }
        }
    }
}