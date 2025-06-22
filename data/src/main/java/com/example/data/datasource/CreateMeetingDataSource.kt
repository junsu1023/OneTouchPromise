package com.example.data.datasource

import com.example.data.entity.CreateMeetingEntity
import com.example.domain.error.CreateMeetingError
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CreateMeetingDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun createMeeting(entity: CreateMeetingEntity): Result<Unit> = try {
        val newDoc = firestore.collection("meetings").document()
        newDoc.set(entity).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(CreateMeetingError.Unknown(e.message))
    }
}