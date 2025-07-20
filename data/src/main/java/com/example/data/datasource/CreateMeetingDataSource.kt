package com.example.data.datasource

import com.example.data.entity.CreateMeetingEntity
import com.example.domain.error.CreateMeetingError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
            val document = firestore.collection("meetings").document()
            val userEmail = user.email ?: return@withContext Result.failure(CreateMeetingError.Unknown("No user email"))

            val emailList = meeting.participants
            val nicknameMap = meeting.participantNicknames

            val userDoc = firestore.collection("users").document(user.uid).get().await()
            val nickname = userDoc.getString("nickname") ?: "unknown"

            val finalEmailList = if(userEmail !in emailList) {
                emailList + userEmail
            } else {
                emailList
            }

            val finalNicknameMap = nicknameMap.toMutableMap().apply { this[userEmail] = nickname }

            val meetingWithId = meeting.copy(
                id = document.id,
                ownerId = user.uid,
                creatorEmail = user.email ?: "unknown",
                participants = finalEmailList,
                participantNicknames = finalNicknameMap,
                dueDate = meeting.dueDate
            )

            val meetingMap = hashMapOf(
                "title" to meeting.title,
                "participants" to meeting.participants,
                "timestamp" to FieldValue.serverTimestamp()
            )

            document.set(meetingWithId).await()
            firestore.collection("meetings").add(meetingMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(CreateMeetingError.Unknown(e.message))
        }
    }
}