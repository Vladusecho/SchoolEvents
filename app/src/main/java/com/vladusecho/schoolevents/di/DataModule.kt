package com.vladusecho.schoolevents.di

import android.content.Context
import androidx.room.Room
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.data.local.EventsAppDatabase
import com.vladusecho.schoolevents.data.repository.AuthRepositoryImpl
import com.vladusecho.schoolevents.data.repository.EventsRepositoryImpl
import com.vladusecho.schoolevents.data.repository.ProfileRepositoryImpl
import com.vladusecho.schoolevents.domain.repository.AuthRepository
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindEventsRepository(impl: EventsRepositoryImpl): EventsRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideEventsAppDatabase(
            @ApplicationContext context: Context
        ): EventsAppDatabase {
            return Room.databaseBuilder(
                context,
                EventsAppDatabase::class.java,
                "events_app_database"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        @Singleton
        fun provideEventsAppDao(eventsAppDatabase: EventsAppDatabase): EventsAppDao {
            return eventsAppDatabase.eventsAppDao()
        }
    }
}