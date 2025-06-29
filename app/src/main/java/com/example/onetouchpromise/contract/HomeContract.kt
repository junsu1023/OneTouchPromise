package com.example.onetouchpromise.contract

import com.example.domain.model.MeetingModel

data class HomeUiState(
    val meetings: List<MeetingModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)