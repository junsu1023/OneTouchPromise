package com.example.domain.usecase

import com.example.domain.model.HomeMeetingModel
import com.example.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class ObserveHomeMeetingsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(
        onEvent: (Result<List<HomeMeetingModel>>) -> Unit
    ): ListenerRegistration = homeRepository.observeUserMeeting(onEvent)
}