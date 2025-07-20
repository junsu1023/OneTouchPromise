package com.example.onetouchpromise.contract

sealed class FriendRequestContract {
    object Idle: FriendRequestContract()
    object Loading: FriendRequestContract()
    object AlreadyFriends: FriendRequestContract()
    object AlreadySent : FriendRequestContract()
    object AlreadyReceived : FriendRequestContract()
    object Sent : FriendRequestContract()
}