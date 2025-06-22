package com.example.data.repository

import com.example.data.datasource.CreateMeetingDataSource
import com.example.data.entity.CreateMeetingEntity
import com.example.data.entity.CreateVoteOptionEntity
import com.example.domain.model.CreateMeetingModel
import com.example.domain.model.VoteType
import com.example.domain.repository.CreateMeetingRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CreateMeetingRepositoryImpl @Inject constructor(
    private val dataSource: CreateMeetingDataSource,
    private val auth: FirebaseAuth
): CreateMeetingRepository {
    override suspend fun createMeeting(model: CreateMeetingModel): Result<Unit> {
        val voteOptions = model.dateOptions.map {
            CreateVoteOptionEntity(
                option = it,
                type = VoteType.DATE
            )
        } + model.locationOptions.map {
            CreateVoteOptionEntity(
                option = it,
                type = VoteType.LOCATION
            )
        }

        val entity = CreateMeetingEntity(
            title = model.title,
            creatorEmail = model.creatorEmail,
            participants = model.participants,
            voteOptions = voteOptions
        )

        return dataSource.createMeeting(entity)
    }
}