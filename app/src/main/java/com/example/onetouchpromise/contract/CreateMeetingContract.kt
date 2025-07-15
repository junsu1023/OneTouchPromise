package com.example.onetouchpromise.contract

import com.example.domain.error.CreateMeetingError
import com.example.domain.model.CreateMeetingModel
import com.example.domain.model.UserModel

data class CreateMeetingUiState(
    val title: String = "",
    val dateOptions: List<String> = emptyList(),
    val locationOptions: List<String> = emptyList(),
    val participants: List<UserModel> = emptyList(),
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