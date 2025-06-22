package com.example.data.datasource

import com.example.data.entity.HomeMeetingEntity
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
        onEvent: (Result<List<HomeMeetingEntity>>) -> Unit
    ): ListenerRegistration {
        val email = auth.currentUser?.email
            ?: return DummyListenerRegistration(onEvent)

        return firestore.collection("meetings")
            .whereArrayContains("participants", email)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onEvent(Result.failure(error))
                    return@addSnapshotListener
                }

                val meetings = snapshot?.documents
                    ?.mapNotNull { it.toObject(HomeMeetingEntity::class.java) }
                    .orEmpty()

                onEvent(Result.success(meetings))
            }
    }

    private fun DummyListenerRegistration(
        onEvent: (Result<List<HomeMeetingEntity>>) -> Unit
    ): ListenerRegistration {
        onEvent(Result.failure(CreateMeetingError.NotLoggedIn))
        return ListenerRegistration { }
    }
}