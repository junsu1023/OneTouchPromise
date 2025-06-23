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
        date = date,
        participantCount = participants.size
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
    dueDate = dueDate
)

fun MeetingDetailEntity.toModel(meetingId: String): MeetingDetailModel = MeetingDetailModel(
    id = meetingId,
    title = this.title,
    creatorEmail = this.creatorEmail,
    participants = this.participants,
    voteOptions = this.voteOptions.map { it.toModel() },
    dueDate = dueDate
)

fun MeetingDetailModel.toHomeModel(): HomeMeetingModel {
    val totalVotes = voteOptions.flatMap { it.votedUserIds }.distinct().size
    val participantCount = participants.size.coerceAtLeast(1)

    return HomeMeetingModel(
        id = id,
        title = title,
        creatorEmail = creatorEmail,
        dueDate = dueDate,
        dateOptions = voteOptions.filter { it.type == VoteType.DATE }.map { it.option },
        locationOptions = voteOptions.filter { it.type == VoteType.LOCATION }.map { it.option },
        voteRatio = totalVotes.toFloat() / participantCount
    )
}

fun VoteOptionEntity.toModel(): VoteOptionModel = VoteOptionModel(
    type = VoteType.valueOf(type),
    option = option,
    votedUserIds = votedUserIds
)

fun CreateMeetingEntity.toHomeMeetingEntity(): HomeMeetingEntity {
    return HomeMeetingEntity(
        id = id,
        title = title,
        ownerId = ownerId,
        creatorEmail = creatorEmail,
        participants = participants,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        dueDate = dueDate, // ✅ 반영
        createdAt = createdAt
    )
}