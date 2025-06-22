package com.example.domain.usecase

import com.example.domain.error.CreateMeetingError
import com.example.domain.model.CreateMeetingModel
import com.example.domain.repository.CreateMeetingRepository
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val createMeetingRepository: CreateMeetingRepository
) {
    suspend operator fun invoke(model: CreateMeetingModel): Result<Unit> {
        return when {
            model.title.isBlank() -> Result.failure(CreateMeetingError.EmptyTitle)
            model.dateOptions.isEmpty() -> Result.failure(CreateMeetingError.NoVoteOptions)
            model.locationOptions.isEmpty() -> Result.failure(CreateMeetingError.NoVoteOptions)
            model.participants.isEmpty() -> Result.failure(CreateMeetingError.NoParticipants)
            else -> createMeetingRepository.createMeeting(model)
        }
    }
}