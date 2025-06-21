package com.example.data.di

import com.example.data.datasource.AuthDataSource
import com.example.data.datasource.MeetingDataSource
import com.example.data.datasource.MeetingDetailRemoteDataSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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
        firebaseAuth: FirebaseAuth
    ): AuthDataSource = AuthDataSource(firebaseAuth)

    @Provides
    @Singleton
    fun provideMeetingDetailDataSource(
        firestore: FirebaseFirestore
    ): MeetingDetailRemoteDataSource = MeetingDetailRemoteDataSource(firestore)
}