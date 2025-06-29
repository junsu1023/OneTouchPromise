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
    val voteOptions = dateOptions.map {
        VoteOptionEntity(type = "DATE", option = it, votedUserIds = emptyList())
    } + locationOptions.map {
        VoteOptionEntity(type = "LOCATION", option = it, votedUserIds = emptyList())
    }

    return MeetingModel(
        id = id,
        title = title,
        creatorEmail = creatorEmail,
        participants = participants,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        voteOptions = voteOptions.map { it.toModel() },
        dueDate = dueDate,
        createdAt = createdAt
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

fun CreateMeetingModel.toEntity(id: String, creatorEmail: String): CreateMeetingEntity {
    val voteOptions = dateOptions.map {
        VoteOptionEntity(type = "DATE", option = it, votedUserIds = emptyList())
    } + locationOptions.map {
        VoteOptionEntity(type = "LOCATION", option = it, votedUserIds = emptyList())
    }

    return CreateMeetingEntity(
        id = id,
        title = title,
        ownerId = id,
        creatorEmail = creatorEmail,
        participants = participants,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        voteOptions = voteOptions,
        createdAt = System.currentTimeMillis()
    )
}

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

fun CreateMeetingModel.toMeetingModel(): MeetingModel {
    val voteOptions = dateOptions.map {
        VoteOptionModel(type = VoteType.DATE, option = it, votedUserIds = emptyList())
    } + locationOptions.map {
        VoteOptionModel(type = VoteType.LOCATION, option = it, votedUserIds = emptyList())
    }

    return MeetingModel(
        id = id,
        title = title,
        creatorEmail = creatorEmail,
        participants = participants,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        voteOptions = voteOptions,
        dueDate = dueDate,
        createdAt = createdAt
    )
}

fun CreateMeetingEntity.toMeetingEntity(): MeetingEntity {
    val voteOptions = dateOptions.map {
        VoteOptionEntity(type = "DATE", option = it, votedUserIds = emptyList())
    } + locationOptions.map {
        VoteOptionEntity(type = "LOCATION", option = it, votedUserIds = emptyList())
    }

    return MeetingEntity(
        id = id,
        title = title,
        ownerId = ownerId,
        creatorEmail = creatorEmail,
        participants = participants,
        voteOptions = voteOptions,
        dueDate = dueDate,
        createdAt = createdAt
    )
}

fun VoteOptionEntity.toModel(): VoteOptionModel = VoteOptionModel(
    type = when (type) {
        "DATE" -> VoteType.DATE
        "LOCATION" -> VoteType.LOCATION
        else -> throw IllegalArgumentException("Unknown vote type: $type")
    },
    option = option,
    votedUserIds = votedUserIds
)

fun VoteOptionModel.toEntity(): VoteOptionEntity = VoteOptionEntity(
    type = when (type) {
        VoteType.DATE -> "DATE"
        VoteType.LOCATION -> "LOCATION"
    },
    option = option,
    votedUserIds = votedUserIds
)