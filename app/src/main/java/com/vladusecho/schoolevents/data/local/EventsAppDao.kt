package com.vladusecho.schoolevents.data.local

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladusecho.schoolevents.data.local.model.EventModel
import com.vladusecho.schoolevents.data.local.model.FavouriteEventModel
import com.vladusecho.schoolevents.data.local.model.NewsModel
import com.vladusecho.schoolevents.data.local.model.ProfileModel
import com.vladusecho.schoolevents.data.local.model.SubscribedEventModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsAppDao {

    @Query("SELECT * FROM profile WHERE email = :email")
    fun getProfile(email: String): Flow<ProfileModel>

    @Query("SELECT * FROM profile WHERE email = :email")
    suspend fun getProfileOnce(email: String): ProfileModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: ProfileModel)

    @Query("SELECT EXISTS(SELECT 1 FROM profile WHERE email = :email)")
    suspend fun checkUserExists(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM profile WHERE email = :email AND password = :password)")
    suspend fun checkUserPassword(email: String, password: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventModel)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Int)

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): EventModel

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsModel

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        WHERE e.id = :eventId
    """)
    suspend fun getEventWithStatusById(eventId: Int, userEmail: String): EventWithStatus

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        WHERE e.isArchived = 0 AND e.status = 'APPROVED'
    """)
    fun getEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        WHERE e.creatorEmail = :creatorEmail
    """)
    fun getEventsByCreator(creatorEmail: String, userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        INNER JOIN subscribed_events s ON e.id = s.eventId
        WHERE s.userEmail = :userEmail AND e.isArchived = 0
    """)
    fun getSubscribedEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        INNER JOIN favourite_events f ON e.id = f.eventId
        WHERE f.userEmail = :userEmail
    """)
    fun getFavouriteEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        WHERE e.isArchived = 1
    """)
    fun getArchivedEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed
        FROM events e
        WHERE e.status = 'PENDING'
    """)
    fun getPendingEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("UPDATE events SET status = :status WHERE id = :eventId")
    suspend fun updateEventStatus(eventId: Int, status: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun subscribeToEvent(subscribedEvent: SubscribedEventModel)

    @Query("DELETE FROM subscribed_events WHERE userEmail = :userEmail AND eventId = :eventId")
    suspend fun unsubscribeFromEvent(userEmail: String, eventId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteEvent(favouriteEvent: FavouriteEventModel)

    @Query("DELETE FROM favourite_events WHERE userEmail = :userEmail AND eventId = :eventId")
    suspend fun removeFavouriteEvent(userEmail: String, eventId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsModel)

    @Query("SELECT * FROM news ORDER BY id DESC")
    fun getNews(): Flow<List<NewsModel>>

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun deleteNews(newsId: Int)

    @Query("""
        SELECT p.* FROM profile p
        INNER JOIN subscribed_events s ON p.email = s.userEmail
        WHERE s.eventId = :eventId
    """)
    fun getParticipants(eventId: Int): Flow<List<ProfileModel>>
}

data class EventWithStatus(
    @Embedded val event: EventModel,
    val isFavourite: Boolean,
    val isSubscribed: Boolean
)
