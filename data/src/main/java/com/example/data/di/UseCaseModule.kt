package com.example.data.di

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.MeetingRepository
import com.example.domain.usecase.GetMeetingsUseCase
import com.example.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetMeetingsUseCase(
        meetingRepository: MeetingRepository
    ): GetMeetingsUseCase = GetMeetingsUseCase(meetingRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        authRepository: AuthRepository
    ): SignUpUseCase = SignUpUseCase(authRepository)
}