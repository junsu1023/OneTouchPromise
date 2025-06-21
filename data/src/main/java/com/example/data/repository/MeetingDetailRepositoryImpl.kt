package com.example.data.repository

import com.example.data.datasource.MeetingDetailRemoteDataSource
import com.example.data.mapper.toModel
import com.example.domain.error.MeetingDetailError
import com.example.domain.error.mapToMeetingError
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.VoteOptionModel
import com.example.domain.repository.MeetingDetailRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MeetingDetailRepositoryImpl @Inject constructor(
    private val meetingDetailRemoteDataSource: MeetingDetailRemoteDataSource,
    private val auth: FirebaseAuth
): MeetingDetailRepository {
    override suspend fun getMeetingDetail(meetingId: String): Result<MeetingDetailModel> = try {
        val entity = meetingDetailRemoteDataSource.getMeetingDetail(meetingId).getOrThrow()
        Result.success(entity.toModel(meetingId))
    } catch (e: Exception) {
        Result.failure(mapToMeetingError(e))
    }

    override suspend fun submitVote(
        meetingId: String,
        dateOption: VoteOptionModel,
        locationOption: VoteOptionModel
    ): Result<Unit> {
        val userId = auth.currentUser?.email ?: return Result.failure(MeetingDetailError.NotLoggedIn)

        return meetingDetailRemoteDataSource.submitVote(
            meetingId = meetingId,
            dateOption = dateOption.option,
            locationOption = locationOption.option,
            userId = userId
        ).mapCatching {
            it
        }.recoverCatching {
            throw mapToMeetingError(it)
        }
    }

}