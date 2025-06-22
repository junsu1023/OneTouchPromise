package com.example.onetouchpromise.Contract

import com.example.domain.model.HomeMeetingModel

data class HomeUiState(
    val meetings: List<HomeMeetingModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)