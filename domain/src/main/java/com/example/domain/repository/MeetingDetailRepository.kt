package com.example.domain.repository

import com.example.domain.model.MeetingDetailModel

interface MeetingDetailRepository {
    suspend fun getMeetingDetail(meetingId: String): Result<MeetingDetailModel>

    suspend fun submitVote(
        meetingId: String,
        dateOption: String,
        locationOption: String
    ): Result<Unit>
}