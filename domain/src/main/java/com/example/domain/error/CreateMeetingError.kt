package com.example.domain.error

sealed class CreateMeetingError: Throwable() {
    data object EmptyTitle: CreateMeetingError()
    data object NoVoteOptions: CreateMeetingError()
    data object NoParticipants: CreateMeetingError()
    data object DuplicateDateOption: CreateMeetingError()
    data object DuplicateLocationOption: CreateMeetingError()
    data object DuplicateParticipantOption: CreateMeetingError()
    data class Unknown(val msg: String?): CreateMeetingError()
}
