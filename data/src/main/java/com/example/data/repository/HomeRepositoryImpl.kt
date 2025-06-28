package com.example.data.repository

import com.example.data.datasource.HomeDataSource
import com.example.data.mapper.toHomeMeetingModel
import com.example.domain.model.HomeMeetingModel
import com.example.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
): HomeRepository {
    override fun observeUserMeeting(onEvent: (Result<List<HomeMeetingModel>>) -> Unit): ListenerRegistration =
        homeDataSource.observeUserMeetings { result ->
            val mapped = result.map { list -> list.map { it.toHomeMeetingModel() } }
            onEvent(mapped)
        }
}