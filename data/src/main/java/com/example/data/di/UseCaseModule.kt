package com.example.data.di

import com.example.domain.repository.MeetingRepository
import com.example.domain.usecase.GetMeetingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetMeetingsUseCase(
        meetingRepository: MeetingRepository
    ): GetMeetingsUseCase = GetMeetingsUseCase(meetingRepository)
}