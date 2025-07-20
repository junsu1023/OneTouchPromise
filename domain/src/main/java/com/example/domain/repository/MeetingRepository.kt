package com.example.domain.repository

import com.example.domain.status.MeetingResult
import kotlinx.coroutines.flow.Flow

interface MeetingRepository {
    suspend fun getMeeting(): Flow<MeetingResult>
}