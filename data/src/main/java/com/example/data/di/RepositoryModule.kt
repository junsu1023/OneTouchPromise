package com.example.data.di

import com.example.data.datasource.MeetingDataSource
import com.example.data.repository.MeetingRepositoryImpl
import com.example.domain.repository.MeetingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideMeetingRepository(
        meetingDataSource: MeetingDataSource
    ): MeetingRepository = MeetingRepositoryImpl(meetingDataSource)
}