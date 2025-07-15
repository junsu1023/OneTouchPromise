package com.example.data.mapper

import com.example.data.entity.CreateMeetingEntity
import com.example.data.entity.MeetingDetailEntity
import com.example.data.entity.MeetingEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.VoteOptionEntity
import com.example.domain.model.CreateMeetingModel
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.MeetingModel
import com.example.domain.model.MeetingResultModel
import com.example.domain.model.UserModel
import com.example.domain.model.VoteOptionModel
import com.example.domain.model.VoteResultItem
import com.example.domain.model.VoteType

fun MeetingEntity.toModel(today: String? = null): MeetingModel {
    return MeetingModel(
        id = id,
        title = title,
        creatorEmail = creatorEmail,
        participants = participants,
        dateOptions = dateOptions,
        locationOptions = locationOptions,
        dueDate = dueDate,
        createdAt = createdAt,
        alreadyVotes = alreadyVotes,
        voteRatio = if(alreadyVotes.isEmpty()) 0f else alreadyVotes.size / participants.size.toFloat(),
        isClosed = (today != null && dueDate < today) || (alreadyVotes.isNotEmpty() && alreadyVotes.size / participants.size.toFloat() == 1f)
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
        participants = participants.map { it.toEntity() },
        dateOptions = dateOptions,
        dueDate = dueDate,
        locationOptions = locationOptions,
        voteOptions = voteOptions,
        createdAt = System.currentTimeMillis(),
        alreadyVotes = alreadyVotes
    )
}

fun UserModel.toEntity(): UserEntity = UserEntity(
    uid = id,
    email = email,
    nickname = nickname
)

fun MeetingDetailEntity.toModel(meetingId: String): MeetingDetailModel = MeetingDetailModel(
    id = meetingId,
    title = title,
    creatorEmail = creatorEmail,
    participants = participants,
    dateOptions = dateOptions,
    locationOptions = locationOptions,
    voteOptions = if(voteOptions.isEmpty()) createVoteOption(dateOptions, locationOptions) else voteOptions.map { it.toModel() },
    dueDate = dueDate,
    alreadyVotes = alreadyVotes
)

fun MeetingDetailModel.toMeetingResultModel(): MeetingResultModel {
    val dateResults = voteOptions
        .filter { it.type == VoteType.DATE }
        .map {
            VoteResultItem(
                option = it.option,
                voteCount = it.votedUserIds.size,
                voters = it.votedUserIds
            )
        }

    val locationResults = voteOptions
        .filter { it.type == VoteType.LOCATION }
        .map {
            VoteResultItem(
                option = it.option,
                voteCount = it.votedUserIds.size,
                voters = it.votedUserIds
            )
        }

    return MeetingResultModel(
        title = title,
        dateResults = dateResults,
        locationResults = locationResults
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

fun createVoteOption(dateOptions: List<String>, locationOptions: List<String>): List<VoteOptionModel> {
    val list = mutableListOf<VoteOptionModel>()
    dateOptions.forEach {
        list.add(
            VoteOptionModel(
                type = VoteType.DATE,
                option = it,
                votedUserIds = emptyList()
            )
        )
    }

    locationOptions.forEach {
        list.add(
            VoteOptionModel(
                type = VoteType.DATE,
                option = it,
                votedUserIds = emptyList()
            )
        )
    }

    return list
}