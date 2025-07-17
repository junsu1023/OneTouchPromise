package com.example.data.di

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CreateMeetingRepository
import com.example.domain.repository.HomeRepository
import com.example.domain.repository.MeetingDetailRepository
import com.example.domain.repository.MeetingRepository
import com.example.domain.usecase.ChangePasswordUseCase
import com.example.domain.usecase.CreateMeetingUseCase
import com.example.domain.usecase.GetCurrentUserUserCase
import com.example.domain.usecase.GetMeetingDetailUseCase
import com.example.domain.usecase.GetMeetingsUseCase
import com.example.domain.usecase.GetUserByEmailUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.ObserveHomeMeetingsUseCase
import com.example.domain.usecase.SignUpUseCase
import com.example.domain.usecase.SubmitVoteUseCase
import com.example.domain.usecase.UpdateNicknameUseCase
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

    @Provides
    @Singleton
    fun provideCreateMeetingUseCase(
        createMeetingRepository: CreateMeetingRepository
    ): CreateMeetingUseCase = CreateMeetingUseCase(createMeetingRepository)

    @Provides
    @Singleton
    fun provideObserveHomeMeetingUseCase(
        homeRepository: HomeRepository
    ): ObserveHomeMeetingsUseCase = ObserveHomeMeetingsUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository
    ): GetCurrentUserUserCase = GetCurrentUserUserCase(authRepository)

    @Provides
    @Singleton
    fun provideGetUserByEmailUseCase(
        authRepository: AuthRepository
    ): GetUserByEmailUseCase = GetUserByEmailUseCase(authRepository)

    @Provides
    @Singleton
    fun provideUpdateNicknameUseCase(
        authRepository: AuthRepository
    ): UpdateNicknameUseCase = UpdateNicknameUseCase(authRepository)

    @Provides
    @Singleton
    fun provideChangePasswordUseCase(
        authRepository: AuthRepository
    ): ChangePasswordUseCase = ChangePasswordUseCase(authRepository)
}