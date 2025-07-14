package com.example.data.di

import com.example.data.datasource.AuthDataSource
import com.example.data.datasource.CreateMeetingDataSource
import com.example.data.datasource.HomeDataSource
import com.example.data.datasource.MeetingDataSource
import com.example.data.datasource.MeetingDetailRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideMeetingDatasource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): MeetingDataSource = MeetingDataSource(firestore, auth)

    @Provides
    @Singleton
    fun provideAuthDataSource(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthDataSource = AuthDataSource(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideMeetingDetailDataSource(
        firestore: FirebaseFirestore
    ): MeetingDetailRemoteDataSource = MeetingDetailRemoteDataSource(firestore)

    @Provides
    @Singleton
    fun provideCreateMeetingDataSource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CreateMeetingDataSource = CreateMeetingDataSource(firestore, auth)

    @Provides
    @Singleton
    fun provideHomeDataSource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): HomeDataSource = HomeDataSource(firestore, auth)
}