package com.example.domain.repository

import com.example.domain.model.MeetingModel
import kotlinx.coroutines.flow.Flow

interface MeetingRepository {
    suspend fun getMeeting(): Flow<List<MeetingModel>>
}