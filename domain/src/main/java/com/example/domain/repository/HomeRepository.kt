package com.example.domain.repository

import com.example.domain.model.MeetingModel
import com.google.firebase.firestore.ListenerRegistration

interface HomeRepository {
    fun observeUserMeeting(
        onEvent: (Result<List<MeetingModel>>) -> Unit
    ): ListenerRegistration
}