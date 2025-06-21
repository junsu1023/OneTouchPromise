package com.example.domain.repository

import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.VoteOptionModel

interface MeetingDetailRepository {
    suspend fun getMeetingDetail(meetingId: String): Result<MeetingDetailModel>

    suspend fun submitVote(
        meetingId: String,
        dateOption: VoteOptionModel,
        locationOption: VoteOptionModel
    ): Result<Unit>
}