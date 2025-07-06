package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mapper.toMeetingResultModel
import com.example.domain.error.MeetingDetailError
import com.example.domain.usecase.GetMeetingDetailUseCase
import com.example.onetouchpromise.contract.MeetingResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingResultViewModel @Inject constructor(
    private val getMeetingDetailUseCase: GetMeetingDetailUseCase
): ViewModel() {
    var uiState by mutableStateOf(MeetingResultUiState())
        private set

    fun loadMeetingResult(meetingId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val result = getMeetingDetailUseCase(meetingId)

            uiState = when {
                result.isSuccess -> {
                    val entity = result.getOrNull()!!
                    val model = entity.toMeetingResultModel()
                    MeetingResultUiState(meeting = model, isLoading = false)
                }
                else -> {
                    val error = result.exceptionOrNull() as? MeetingDetailError
                    uiState.copy(error = error, isLoading = false)
                }
            }
        }
    }
}