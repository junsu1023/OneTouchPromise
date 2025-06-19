package com.example.data.datasource

import com.example.data.entity.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(email: String, password: String): Result<UserEntity> = try {
        val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
        Result.success(UserEntity(user!!.uid, user.email))
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> = try {
        val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        Result.success(UserEntity(user!!.uid, user.email))
    } catch (e: Exception) {
        if(e is CancellationException) throw e
        Result.failure(e)
    }

    fun getCurrentUser(): UserEntity? {
        val user = firebaseAuth.currentUser ?: return null
        return UserEntity(user.uid, user.email)
    }
}