package com.vladusecho.schoolevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vladusecho.schoolevents.data.local.model.EventModel
import com.vladusecho.schoolevents.data.local.model.FavouriteEventModel
import com.vladusecho.schoolevents.data.local.model.ProfileModel
import com.vladusecho.schoolevents.data.local.model.SubscribedEventModel

@Database(
    entities = [
        ProfileModel::class,
        EventModel::class,
        SubscribedEventModel::class,
        FavouriteEventModel::class
    ],
    version = 3,
    exportSchema = false
)
abstract class EventsAppDatabase : RoomDatabase()  {

    abstract fun eventsAppDao(): EventsAppDao
}
