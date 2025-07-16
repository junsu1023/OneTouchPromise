package com.example.data.datasource

import com.example.data.entity.CreateMeetingEntity
import com.example.domain.error.CreateMeetingError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CreateMeetingDataSource(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun createMeeting(meeting: CreateMeetingEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser?: return@withContext Result.failure(CreateMeetingError.NotLoggedIn)

            val meetingDocument = firestore.collection("meetings").document()
            val userEmail = user.email

            val userDocument = firestore.collection("users").document(user.uid).get().await()
            val nickname = userDocument.getString("nickname") ?: ""

            val meetingWithId = meeting.copy(
                id = meetingDocument.id,
                ownerId = user.uid,
                creatorEmail = user.email ?: "unknown",
                participants = if(userEmail !in meeting.participants.map { it }) {
                    meeting.participants + nickname
                } else {
                    meeting.participants
                },
                dueDate = meeting.dueDate
            )

            meetingDocument.set(meetingWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(CreateMeetingError.Unknown(e.message))
        }
    }
}