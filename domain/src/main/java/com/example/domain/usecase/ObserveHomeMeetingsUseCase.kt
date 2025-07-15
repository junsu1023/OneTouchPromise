package com.example.domain.usecase

import com.example.domain.model.MeetingModel
import com.example.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import java.time.LocalDate
import javax.inject.Inject

class ObserveHomeMeetingsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(
        onEvent: (Result<List<MeetingModel>>) -> Unit
    ): ListenerRegistration {
        val today = LocalDate.now().toString()
        return homeRepository.observeUserMeeting(onEvent, today)
    }
}