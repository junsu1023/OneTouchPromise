package com.example.data.datasource

import android.net.Uri
import com.example.data.entity.UserEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(
        email: String,
        nickname: String,
        password: String,
        profileImageUri: Uri
    ): Result<UserEntity> = try {
        val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            ?: throw IllegalStateException("User Signup Failed")

        val uid = user.uid
        val imageRef = Firebase.storage.reference.child("profile_images/$uid.jpg")
        imageRef.putFile(profileImageUri).await()

        val imageUrl = imageRef.downloadUrl.await().toString()

        val userMap = mapOf(
            "uid" to uid,
            "email" to email,
            "nickname" to nickname,
            "password" to password,
            "profileImageUrl" to imageUrl
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