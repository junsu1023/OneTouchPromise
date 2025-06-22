package com.example.domain.repository

import com.example.domain.model.CreateMeetingModel

interface CreateMeetingRepository {
    suspend fun createMeeting(model: CreateMeetingModel): Result<Unit>
}