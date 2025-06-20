package com.example.onetouchpromise.Contract

import com.example.domain.error.MeetingError
import com.example.domain.model.MeetingModel

data class HomeUiState(
    val meetings: List<MeetingModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: MeetingError? = null
)