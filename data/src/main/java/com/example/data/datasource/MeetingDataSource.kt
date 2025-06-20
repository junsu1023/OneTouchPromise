package com.example.data.datasource

import com.example.data.entity.MeetingEntity
import com.example.domain.error.MeetingError
import com.example.data.mapper.toModel
import com.example.domain.result.MeetingResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MeetingDataSource(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getMeeting(): Flow<MeetingResult> = callbackFlow {
        val uid = auth.currentUser?.uid
        if(uid == null) {
            trySend(MeetingResult.Failure(MeetingError.UserNotLoggedIn))
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("meetings")
            .whereArrayContains("participants", uid)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    trySend(MeetingResult.Failure(MeetingError.NetworkError(error.message)))
                    return@addSnapshotListener
                }

                val meetings = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MeetingEntity::class.java)?.copy(id = doc.id)
                }.orEmpty()

                trySend(MeetingResult.Success(meetings.map { it.toModel() }))
            }

        awaitClose { listener.remove() }
    }
}