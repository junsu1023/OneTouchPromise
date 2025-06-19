package com.example.data.mapper

import com.example.data.entity.MeetingEntity
import com.example.domain.model.MeetingModel

fun MeetingModel.convertEntity(): MeetingEntity = MeetingEntity(
    this.id,
    this.title,
    this.dueDate,
    this.votedCount,
    this.totalCount
)

fun MeetingEntity.convertModel(): MeetingModel = MeetingModel(
    this.id,
    this.title,
    this.dueDate,
    this.votedCount,
    this.totalCount
)