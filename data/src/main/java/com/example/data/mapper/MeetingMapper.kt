package com.example.data.mapper

import com.example.data.entity.CreateMeetingEntity
import com.example.data.entity.HomeMeetingEntity
import com.example.data.entity.MeetingDetailEntity
import com.example.data.entity.MeetingEntity
import com.example.data.entity.VoteOptionEntity
import com.example.domain.model.CreateMeetingModel
import com.example.domain.model.HomeMeetingModel
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.MeetingModel
import com.example.domain.model.VoteOptionModel
import com.example.domain.model.VoteType

fun MeetingEntity.toModel(): MeetingModel {
    return MeetingModel(
        id = id,
        title = title,
        date = dueDate,
        participantCount = participants.size
    )
}

fun MeetingEntity.toHomeMeetingModel(): HomeMeetingModel {
    val totalVotes = dateOptions.size + locationOptions.size
    val voteCount = 0f

    return HomeMeetingModel(
        id = id,
        title = title,
        creatorEmail = creatorEmail,
        dueDate = dueDate,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        voteRatio = voteCount / (totalVotes.takeIf { it != 0 } ?: 1)
    )
}

fun CreateMeetingModel.toEntity(id: String, ownerId: String): CreateMeetingEntity = CreateMeetingEntity(
    id = id,
    title = title,
    ownerId = ownerId,
    creatorEmail = creatorEmail,
    participants = participants,
    dateOptions = dateOptions,
    locationOptions = locationOptions,
    createdAt = System.currentTimeMillis()
)

fun HomeMeetingEntity.toModel(): HomeMeetingModel = HomeMeetingModel(
    id = id,
    title = title,
    creatorEmail = creatorEmail,
    dateOptions = dateOptions,
    locationOptions = locationOptions,
    dueDate = dueDate,
    voteRatio = if(voteOptions.isEmpty()) 0f else participants.size / voteOptions.size.toFloat()
)

fun MeetingDetailEntity.toModel(meetingId: String): MeetingDetailModel = MeetingDetailModel(
    id = meetingId,
    title = title,
    creatorEmail = creatorEmail,
    participants = participants,
    voteOptions = voteOptions.map { it.toModel() },
    dueDate = dueDate
)

fun VoteOptionEntity.toModel(): VoteOptionModel = VoteOptionModel(
    type = when (type) {
        "DATE" -> VoteType.DATE
        "LOCATION" -> VoteType.LOCATION
        else -> throw IllegalArgumentException("Unknown vote type: $type")
    },
    option = option,
    votedUserIds = votedUserIds
)

fun VoteOptionModel.toEntity(): VoteOptionEntity =
    VoteOptionEntity(
        type = when (type) {
            VoteType.DATE -> "DATE"
            VoteType.LOCATION -> "LOCATION"
        },
        option = option,
        votedUserIds = votedUserIds
    )