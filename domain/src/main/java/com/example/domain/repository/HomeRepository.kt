package com.example.domain.repository

import com.example.domain.model.HomeMeetingModel
import com.google.firebase.firestore.ListenerRegistration

interface HomeRepository {
    fun observeUserMeeting(
        onEvent: (Result<List<HomeMeetingModel>>) -> Unit
    ): ListenerRegistration
}