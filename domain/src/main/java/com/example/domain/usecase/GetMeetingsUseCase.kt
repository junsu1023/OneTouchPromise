package com.example.domain.usecase

import com.example.domain.model.MeetingModel
import com.example.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow

class GetMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): Flow<List<MeetingModel>> = meetingRepository.getMeetingListFlow()
}