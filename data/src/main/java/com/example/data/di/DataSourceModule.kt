package com.example.data.di

import com.example.data.datasource.MeetingDataSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideMeetingDatasource(): MeetingDataSource = MeetingDataSource(Firebase.firestore)
}