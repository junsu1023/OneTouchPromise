package com.example.data.datasource

import com.example.data.entity.MeetingEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MeetingDataSource(
    private val firestore: FirebaseFirestore
) {
    fun getMeetingsFlow(): Flow<List<MeetingEntity>> = callbackFlow {
        val listenerRegistration = firestore.collection("meetings")
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val meetings = snapshot?.mapNotNull { doc ->
                    doc.toObject(MeetingEntity::class.java).copy(id = doc.id)
                } ?: emptyList()

                trySend(meetings)
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}