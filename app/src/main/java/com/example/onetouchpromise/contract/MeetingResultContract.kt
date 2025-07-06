package com.example.onetouchpromise.contract

import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingResultModel

data class MeetingResultUiState(
    val meeting: MeetingResultModel? = null,
    val isLoading: Boolean = false,
    val error: MeetingDetailError? = null
)
