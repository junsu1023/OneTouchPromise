package com.example.data.repository

import com.example.data.datasource.MeetingDataSource
import com.example.data.mapper.toModel
import com.example.domain.model.MeetingModel
import com.example.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDataSource: MeetingDataSource
): MeetingRepository {
    override suspend fun getMeeting(): Flow<List<MeetingModel>> =
        meetingDataSource.getMeeting().map { list -> list.map { it.toModel() } }
}