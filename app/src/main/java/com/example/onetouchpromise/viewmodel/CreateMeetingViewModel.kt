package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.error.CreateMeetingError
import com.example.domain.model.CreateMeetingModel
import com.example.domain.usecase.CreateMeetingUseCase
import com.example.onetouchpromise.Contract.CreateMeetingUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun addDateOption(option: String) {
        if(option.isNotBlank()) {
            uiState = uiState.copy(
                dateOptions = uiState.dateOptions + option,
                newDateOption = ""
            )
        }
    }

    fun updateNewDateOption(input: String) {
        uiState = uiState.copy(newDateOption = input)
    }

    fun addLocationOption(option: String) {
        if (option.isNotBlank()) {
            uiState = uiState.copy(
                locationOptions = uiState.locationOptions + option,
                newLocationOption = ""
            )
        }
    }

    fun updateNewLocationOption(input: String) {
        uiState = uiState.copy(newLocationOption = input)
    }

    fun addParticipant(email: String) {
        if (email.isNotBlank()) {
            uiState = uiState.copy(
                participants = uiState.participants + email,
                newParticipant = ""
            )
        }
    }

    fun updateNewParticipant(input: String) {
        uiState = uiState.copy(newParticipant = input)
    }

    fun createMeeting(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            val model = CreateMeetingModel(
                title = uiState.title,
                dateOptions = uiState.dateOptions,
                locationOptions = uiState.locationOptions,
                participants = uiState.participants,
                creatorEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
            )

            val result = createMeetingUseCase(model)
            uiState = if (result.isSuccess) {
                onSuccess()
                CreateMeetingUiState(isSuccess = true)
            } else {
                uiState.copy(
                    isLoading = false,
                    error = result.exceptionOrNull() as? CreateMeetingError
                )
            }
        }
    }
}