package com.example.domain.error

sealed class MeetingError {
    object UserNotLoggedIn: MeetingError()
    data class NetworkError(val message: String?): MeetingError()
    data class Unknown(val throwable: Throwable?): MeetingError()
}