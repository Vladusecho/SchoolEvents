package com.vladusecho.schoolevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProfileModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EventsAppDatabase : RoomDatabase()  {

    abstract fun eventsAppDao(): EventsAppDao
}
