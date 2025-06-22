package com.example.onetouchpromise.Contract

import com.example.domain.error.CreateMeetingError

data class CreateMeetingUiState(
    val title: String = "",
    val dateOptions: List<String> = emptyList(),
    val newDateOption: String = "",
    val locationOptions: List<String> = emptyList(),
    val newLocationOption: String = "",
    val participants: List<String> = emptyList(),
    val newParticipant: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: CreateMeetingError? = null
)