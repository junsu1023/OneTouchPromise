package com.example.onetouchpromise.Contract

import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingDetailModel

data class MeetingDetailUiState(
    val isLoading: Boolean = true,
    val meeting: MeetingDetailModel? = null,
    val error: MeetingDetailError? = null,
)