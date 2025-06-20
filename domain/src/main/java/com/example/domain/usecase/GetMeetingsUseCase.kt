package com.example.domain.usecase

import com.example.domain.repository.MeetingRepository
import com.example.domain.result.MeetingResult
import kotlinx.coroutines.flow.Flow

class GetMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): Flow<MeetingResult> = meetingRepository.getMeeting()
}