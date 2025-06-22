package com.example.onetouchpromise.Contract

import com.example.domain.error.CreateMeetingError
import com.example.domain.model.CreateMeetingModel

data class CreateMeetingUiState(
    val title: String = "",
    val dateOptions: List<String> = emptyList(),
    val newDateOption: String = "",
    val locationOptions: List<String> = emptyList(),
    val newLocationOption: String = "",
    val participants: List<String> = emptyList(),
    val newParticipant: String = "",
    val dueDate: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: CreateMeetingError? = null
)

fun CreateMeetingUiState.toMeetingModel(): CreateMeetingModel {
    return CreateMeetingModel(
        title = this.title,
        participants = this.participants,
        dateOptions = this.dateOptions,
        locationOptions = this.locationOptions,
        dueDate = dueDate
    )
}