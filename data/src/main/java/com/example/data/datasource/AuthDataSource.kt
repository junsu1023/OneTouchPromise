package com.example.data.datasource

import android.util.Log
import com.example.data.entity.UserEntity
import com.example.data.mapper.toModel
import com.example.domain.model.UserModel
import com.example.domain.status.FriendStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUp(
        email: String,
        nickname: String,
        password: String
    ): Result<UserEntity> = try {
        val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            ?: throw IllegalStateException("User Signup Failed")

        val uid = user.uid

        val userMap = mapOf(
            "uid" to uid,
            "email" to email,
            "nickname" to nickname,
            "password" to password
        )

        Firebase.firestore.collection("users").document(uid).set(userMap).await()

        Result.success(UserEntity(user.uid, user.email ?: "", nickname))
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> = try {
        val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        Result.success(UserEntity(user!!.uid, user.email ?: "", ""))
    } catch (e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    suspend fun getCurrentUser(): UserEntity? {
        val user = firebaseAuth.currentUser ?: return null

        return try {
            val document = firestore.collection("users").document(user.uid).get().await()
            val nickname = document.getString("nickname") ?: ""

            UserEntity(user.uid, user.email ?: "", nickname)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserByEmail(email: String): Result<UserEntity?> = try {
        val query = firestore.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()

        val document = query.documents.firstOrNull()
        Result.success(document?.toObject(UserEntity::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateNickname(newNickname: String): Result<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not authenticated")
            val document = firestore.collection("users").document(userId)

            document.update("nickname", newNickname).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePasswordWithReAuth(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> {
        val user = firebaseAuth.currentUser
        val email = user?.email

        val uid = user?.uid ?: return Result.failure(Exception("User not found"))
        val document = firestore.collection("users").document(uid)

        if(!email.isNullOrEmpty()) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            return try {
                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()
                document.update("password", newPassword).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        return Result.failure(Exception("User not authenticated"))
    }

    suspend fun saveFcmToken(token: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw IllegalStateException("Not logged in")
        firestore.collection("users").document(uid)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .await()
    }

    suspend fun getTokensByEmail(emails: List<String>): List<String> {
        val snapshot = firestore.collection("users")
            .whereIn("email", emails)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("fcmToken") }
    }

    suspend fun searchUserByEmail(email: String): UserEntity? {
        val snapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(UserEntity::class.java)
    }

    suspend fun sendFriendRequest(fromUid: String, toEmail: String): Result<Unit> {
        val toUid = searchUserByEmail(toEmail)?.uid

        val data = mapOf(
            "from" to fromUid,
            "to" to toUid,
            "timeStamp" to FieldValue.serverTimestamp(),
            "status" to "pending"
        )

        return try {
            firestore.collection("friendRequests")
                .add(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkFriendStatus(fromUid: String, toEmail: String): FriendStatus {
        val currentUserDoc = firestore.collection("users").document(fromUid).get().await()
        val toUid = searchUserByEmail(toEmail)?.uid

        val friends = currentUserDoc.get("friends") as? List<String> ?: emptyList()
        val outgoing = currentUserDoc.get("outgoingRequests") as? List<String> ?: emptyList()
        val incoming = currentUserDoc.get("incomingRequests") as? List<String> ?: emptyList()

        return when (toUid) {
            in friends -> FriendStatus.FRIENDS
            in outgoing -> FriendStatus.REQUEST_SENT
            in incoming -> FriendStatus.REQUEST_RECEIVED
            else -> FriendStatus.NONE
        }
    }

    suspend fun acceptFriendRequest(myUid: String, fromUid: String) {
        val myDoc = firestore.collection("users").document(myUid)
        val fromDoc = firestore.collection("users").document(fromUid)

        firestore.runBatch { batch ->
            batch.update(myDoc, "myFriends", FieldValue.arrayUnion(fromUid))
            batch.update(fromDoc, "myFriends", FieldValue.arrayUnion(myUid))

            batch.update(myDoc, "friendRequestsReceived", FieldValue.arrayRemove(fromUid))
            batch.update(fromDoc, "friendRequestsSent", FieldValue.arrayRemove(myUid))
        }.await()
    }

    suspend fun declineFriendRequest(myUid: String, fromUid: String) {
        val myDoc = firestore.collection("users").document(myUid)
        val fromDoc = firestore.collection("users").document(fromUid)

        firestore.runBatch { batch ->
            batch.update(myDoc, "friendRequestsReceived", FieldValue.arrayRemove(fromUid))
            batch.update(fromDoc, "friendRequestsSent", FieldValue.arrayRemove(myUid))
        }.await()
    }

    fun getFriendRequests(myUid: String, onResult: (List<UserModel>) -> Unit) {
        firestore.collection("friendRequests")
            .whereEqualTo("to", myUid)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    Log.w("FriendRequestListener", "Failed.", error)
                    return@addSnapshotListener
                }

                if(snapshot != null && !snapshot.isEmpty) {
                    val uidList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(UserEntity::class.java)
                    }
                    onResult(uidList.map { it.toModel() })
                } else {
                    onResult(emptyList())
                }
            }
    }
}