package com.example.data.datasource

import com.example.data.entity.MeetingEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MeetingDataSource(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getMeeting(): Flow<List<MeetingEntity>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if(uid == null) {
            close(Exception("로그인된 사용자가 없습니다."))
            return@callbackFlow
        }

        val listener = firestore.collection("meetings")
            .whereArrayContains("participants", uid)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val meetings = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MeetingEntity::class.java)?.copy(id = doc.id)
                }.orEmpty()

                trySend(meetings)
            }

        awaitClose { listener.remove() }
    }
}