package com.example.onetouchpromise.Contract

import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.VoteOptionModel

data class MeetingDetailUiState(
    val isLoading: Boolean = true,
    val meeting: MeetingDetailModel? = null,
    val selectedDateOption: VoteOptionModel? = null,
    val selectedLocationOption: VoteOptionModel? = null,
    val error: MeetingDetailError? = null,
    val isVoteCompleted: Boolean = false
)
