package com.vladusecho.schoolevents.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsAppDao {

    @Query("SELECT * FROM profile WHERE email = :email")
    fun getProfile(email: String): Flow<ProfileModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: ProfileModel)

    @Query("SELECT EXISTS(SELECT 1 FROM profile WHERE email = :email)")
    suspend fun checkUserExists(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM profile WHERE email = :email AND password = :password)")
    suspend fun checkUserPassword(email: String, password: String): Boolean
}
