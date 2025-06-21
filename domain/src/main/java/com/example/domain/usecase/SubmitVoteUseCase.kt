package com.example.domain.usecase

import com.example.domain.model.VoteOptionModel
import com.example.domain.repository.MeetingDetailRepository
import javax.inject.Inject

class SubmitVoteUseCase @Inject constructor(
    private val meetingDetailRepository: MeetingDetailRepository
) {
    suspend operator fun invoke(
        meetingId: String,
        dateOption: VoteOptionModel,
        locationOption: VoteOptionModel
    ): Result<Unit> = meetingDetailRepository.submitVote(meetingId, dateOption, locationOption)
}