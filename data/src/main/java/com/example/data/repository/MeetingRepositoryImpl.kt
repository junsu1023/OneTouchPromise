package com.example.data.repository

import com.example.data.datasource.MeetingDataSource
import com.example.domain.repository.MeetingRepository
import com.example.domain.result.MeetingResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDataSource: MeetingDataSource
): MeetingRepository {
    override suspend fun getMeeting(): Flow<MeetingResult> = meetingDataSource.getMeeting()
}