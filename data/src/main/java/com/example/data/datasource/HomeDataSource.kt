package com.example.data.datasource

import com.example.data.entity.MeetingEntity
import com.example.domain.error.CreateMeetingError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun observeUserMeetings(
        onEvent: (Result<List<MeetingEntity>>) -> Unit
    ): ListenerRegistration {
        val email = auth.currentUser?.email
            ?: return dummyListenerRegistration(onEvent)

        return firestore.collection("meetings")
            .whereArrayContains("participants", email)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onEvent(Result.failure(error))
                    return@addSnapshotListener
                }

                val meetings = snapshot?.documents
                    ?.mapNotNull { it.toObject(MeetingEntity::class.java) }
                    .orEmpty()

                onEvent(Result.success(meetings))
            }
    }

    private fun dummyListenerRegistration(
        onEvent: (Result<List<MeetingEntity>>) -> Unit
    ): ListenerRegistration {
        onEvent(Result.failure(CreateMeetingError.NotLoggedIn))
        return ListenerRegistration { }
    }
}