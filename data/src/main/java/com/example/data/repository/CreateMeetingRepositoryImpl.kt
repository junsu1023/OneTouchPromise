package com.example.data.repository

import com.example.data.datasource.CreateMeetingDataSource
import com.example.data.entity.CreateMeetingEntity
import com.example.domain.error.CreateMeetingError
import com.example.domain.model.CreateMeetingModel
import com.example.domain.repository.CreateMeetingRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CreateMeetingRepositoryImpl @Inject constructor(
    private val createMeetingDataSource: CreateMeetingDataSource,
    private val auth: FirebaseAuth
): CreateMeetingRepository {
    override suspend fun createMeeting(model: CreateMeetingModel): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(CreateMeetingError.NotLoggedIn)

        val entity = CreateMeetingEntity(
            title = model.title,
            ownerId = user.uid,
            creatorEmail = user.email ?: "unknown",
            participants = model.participants,
            dateOptions = model.dateOptions,
            locationOptions = model.locationOptions,
            dueDate = model.dueDate
        )

        return createMeetingDataSource.createMeeting(entity)
    }
}