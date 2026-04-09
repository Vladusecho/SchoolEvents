package com.vladusecho.schoolevents.di

import com.vladusecho.schoolevents.data.repository.EventsRepositoryImpl
import com.vladusecho.schoolevents.data.repository.ExampleEventsRepositoryImpl
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindEventsRepository(impl: ExampleEventsRepositoryImpl): EventsRepository

}