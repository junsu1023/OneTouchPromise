package com.example.data.di

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.MeetingDetailRepository
import com.example.domain.repository.MeetingRepository
import com.example.domain.usecase.GetMeetingDetailUseCase
import com.example.domain.usecase.GetMeetingsUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.SignUpUseCase
import com.example.domain.usecase.SubmitVoteUseCase
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

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase = LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetMeetingDetailUseCase(
        meetingDetailRepository: MeetingDetailRepository
    ): GetMeetingDetailUseCase = GetMeetingDetailUseCase(meetingDetailRepository)

    @Provides
    @Singleton
    fun provideSubmitVoteUseCase(
        meetingDetailRepository: MeetingDetailRepository
    ): SubmitVoteUseCase = SubmitVoteUseCase(meetingDetailRepository)
}