package com.example.domain.error

sealed class MeetingDetailError: Throwable() {
    data object NotLoggedIn: MeetingDetailError()
    data object NotFound: MeetingDetailError()
    data class UnKnown(override val message: String?): MeetingDetailError()
}

fun mapToMeetingError(e: Throwable): MeetingDetailError = when(e) {
    is IllegalStateException -> {
        if(e.message?.contains("Meeting not found") == true) {
            MeetingDetailError.NotFound
        } else {
            MeetingDetailError.UnKnown(e.message)
        }
    }
    else -> MeetingDetailError.UnKnown(e.message)
}