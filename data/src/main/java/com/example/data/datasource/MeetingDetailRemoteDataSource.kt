package com.example.data.datasource

import com.example.data.entity.MeetingDetailEntity
import com.example.data.entity.MeetingEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MeetingDetailRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val meetingCollection = firestore.collection("meetings")

    suspend fun getMeetingDetail(meetingId: String): Result<MeetingDetailEntity> = try {
        val snapshot = meetingCollection.document(meetingId).get().await()
        val entity = snapshot.toObject(MeetingDetailEntity::class.java) ?: throw IllegalStateException("Meeting not found")
        Result.success(entity)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun submitVote(
        meetingId: String,
        dateOption: String,
        locationOption: String,
        userId: String
    ): Result<Unit> = try {
        firestore.runTransaction { transaction ->
            val docRef = meetingCollection.document(meetingId)
            val snapshot = transaction[docRef]
            val entity = snapshot.toObject(MeetingDetailEntity::class.java) ?: throw IllegalStateException("Meeting not found")
            val updatedVoteOptions = entity.voteOptions.map {
                val isDate = it.type == "DATE" && it.option == dateOption
                val isLocation = it.type == "LOCATION" && it.option == locationOption

                if(isDate || isLocation) {
                    it.copy(votedUserIds = it.votedUserIds + userId)
                } else it
            }

            transaction.update(docRef, "voteOptions", updatedVoteOptions)
        }.await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}