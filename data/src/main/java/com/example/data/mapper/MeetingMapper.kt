package com.example.data.mapper

import com.example.data.entity.MeetingEntity
import com.example.domain.model.MeetingModel

fun MeetingEntity.toModel(): MeetingModel {
    return MeetingModel(
        id = id,
        title = title,
        date = date,
        participantCount = participants.size
    )
}