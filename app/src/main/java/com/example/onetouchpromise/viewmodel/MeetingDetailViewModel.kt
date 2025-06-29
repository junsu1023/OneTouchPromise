package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.error.MeetingDetailError
import com.example.domain.usecase.GetMeetingDetailUseCase
import com.example.domain.usecase.SubmitVoteUseCase
import com.example.onetouchpromise.contract.MeetingDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val getMeetingDetailUseCase: GetMeetingDetailUseCase,
    private val submitVoteUseCase: SubmitVoteUseCase
): ViewModel() {
    var uiState by mutableStateOf(MeetingDetailUiState())
        private set

    fun loadMeetingDetail(meetingId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val result = getMeetingDetailUseCase(meetingId)
            uiState = when {
                result.isSuccess -> {
                    uiState.copy(
                        meeting = result.getOrNull(),
                        isLoading = false,
                        error = null
                    )
                }
                else -> {
                    uiState.copy(
                        error = result.exceptionOrNull() as MeetingDetailError,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun submitVote(
        meetingId: String,
        date: String,
        location: String
    ) {
        viewModelScope.launch {
            val result = submitVoteUseCase(meetingId, date, location)

            uiState = if(result.isSuccess) {
                uiState.copy(isVoteSuccess = true)
            } else {
                uiState.copy(error = result.exceptionOrNull() as MeetingDetailError)
            }
        }
    }
}