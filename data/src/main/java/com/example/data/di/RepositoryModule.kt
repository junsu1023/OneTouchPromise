package com.example.data.di

import com.example.data.datasource.AuthDataSource
import com.example.data.datasource.MeetingDataSource
import com.example.data.datasource.MeetingDetailRemoteDataSource
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.MeetingDetailRepositoryImpl
import com.example.data.repository.MeetingRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.MeetingDetailRepository
import com.example.domain.repository.MeetingRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideMeetingRepository(
        meetingDataSource: MeetingDataSource
    ): MeetingRepository = MeetingRepositoryImpl(meetingDataSource)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: AuthDataSource
    ): AuthRepository = AuthRepositoryImpl(authDataSource)

    @Provides
    @Singleton
    fun provideMeetingDetailRepository(
        meetingDetailRemoteDataSource: MeetingDetailRemoteDataSource,
        auth: FirebaseAuth
    ): MeetingDetailRepository = MeetingDetailRepositoryImpl(meetingDetailRemoteDataSource, auth)
}