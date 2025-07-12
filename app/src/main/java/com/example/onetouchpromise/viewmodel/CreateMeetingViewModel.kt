package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.error.CreateMeetingError
import com.example.domain.usecase.CreateMeetingUseCase
import com.example.onetouchpromise.contract.CreateMeetingUiState
import com.example.onetouchpromise.contract.toMeetingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreateMeetingViewModel @Inject constructor(
    private val createMeetingUseCase: CreateMeetingUseCase
): ViewModel() {
    var uiState by mutableStateOf(CreateMeetingUiState())
        private set

    fun updateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun updateDueDate(dueDate: LocalDate) {
        val today = LocalDate.now()

        if(dueDate.isBefore(today)) {
            uiState = uiState.copy(error = CreateMeetingError.NotAfterDate)
        } else {
            uiState = uiState.copy(dueDate = dueDate.toString())
        }
    }

    fun addDateOption(date: LocalDate) {
        val today = LocalDate.now()

        if(date.isBefore(today)) {
            uiState = uiState.copy(error = CreateMeetingError.NotAfterDate)
            return
        }

        if(uiState.dateOptions.contains(date.toString())) {
            uiState = uiState.copy(error = CreateMeetingError.DuplicateDateOption)
            return
        }

        if(date.toString().isNotBlank()) {
            uiState = uiState.copy(dateOptions = uiState.dateOptions + date.toString())
        }
    }

    fun removeDateOption(date: String) {
        uiState = uiState.copy(dateOptions = uiState.dateOptions - date)
    }

    fun removeLocationOption(location: String) {
        uiState = uiState.copy(locationOptions = uiState.locationOptions - location)
    }

    fun removeParticipant(email: String) {
        uiState = uiState.copy(participants = uiState.participants - email)
    }

    fun addLocationOption(location: String) {
        if(uiState.locationOptions.contains(location)) {
            uiState = uiState.copy(error = CreateMeetingError.DuplicateLocationOption)
            return
        }

        if (location.isNotBlank()) {
            uiState = uiState.copy(locationOptions = uiState.locationOptions + location)
        }
    }

    fun addParticipant(email: String) {
        if(uiState.dateOptions.contains(email)) {
            uiState = uiState.copy(error = CreateMeetingError.DuplicateParticipantOption)
            return
        }

        if (email.isNotBlank()) {
            uiState = uiState.copy(
                participants = uiState.participants + email,
                newParticipant = ""
            )
        }
    }

    fun createMeeting(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            val model = uiState.toMeetingModel()
            val result = createMeetingUseCase(model)
            uiState = if (result.isSuccess) {
                onSuccess()
                CreateMeetingUiState(isSuccess = true, isLoading = false)
            } else {
                uiState.copy(
                    isLoading = false,
                    error = result.exceptionOrNull() as? CreateMeetingError
                )
            }
        }
    }
}