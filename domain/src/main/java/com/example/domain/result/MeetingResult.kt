package com.example.domain.result

import com.example.domain.error.MeetingError
import com.example.domain.model.MeetingModel

sealed class MeetingResult {
    data class Success(val meetings: List<MeetingModel>): MeetingResult()
    data class Failure(val error: MeetingError): MeetingResult()
}