package com.example.data.mapper

import com.example.data.entity.MeetingDetailEntity
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.VoteOptionModel
import com.example.domain.model.VoteType

fun MeetingDetailEntity.toModel(meetingId: String): MeetingDetailModel = MeetingDetailModel(
    id = meetingId,
    title = this.title,
    creatorEmail = this.creatorEmail,
    participants = this.participants,
    voteOptions = this.voteOptions.map {
        VoteOptionModel(
            type = VoteType.valueOf(it.type),
            option = it.option,
            votedUserIds = it.votedUserIds
        )
    }
)