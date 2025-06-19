package com.example.data.datasource

import com.example.data.entity.UserEntity
import com.example.domain.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): Result<UserEntity> = runCatching {
        val user = auth.createUserWithEmailAndPassword(email, password).await().user
        UserEntity(user!!.uid, user.email)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> = runCatching {
        val user = auth.signInWithEmailAndPassword(email, password).await().user
        UserEntity(user!!.uid, user.email)
    }

    fun getCurrentUser(): UserEntity? {
        val user = auth.currentUser ?: return null
        return UserEntity(user.uid, user.email)
    }
}