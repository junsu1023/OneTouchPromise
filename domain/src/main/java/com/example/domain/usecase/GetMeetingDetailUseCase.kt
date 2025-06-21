package com.example.domain.usecase

import com.example.domain.model.MeetingDetailModel
import com.example.domain.repository.MeetingDetailRepository
import javax.inject.Inject

class GetMeetingDetailUseCase @Inject constructor(
    private val meetingDetailRepository: MeetingDetailRepository
) {
    suspend operator fun invoke(meetingId: String): Result<MeetingDetailModel> = meetingDetailRepository.getMeetingDetail(meetingId)
}