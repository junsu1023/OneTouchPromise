package com.example.data.datasource

import com.example.data.entity.UserEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(email: String, nickname: String, password: String): Result<UserEntity> = try {
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

        Result.success(UserEntity(user.uid, user.email, nickname))
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> = try {
        val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        Result.success(UserEntity(user!!.uid, user.email, null))
    } catch (e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    fun getCurrentUser(): UserEntity? {
        val user = firebaseAuth.currentUser ?: return null
        return UserEntity(user.uid, user.email, null)
    }
}