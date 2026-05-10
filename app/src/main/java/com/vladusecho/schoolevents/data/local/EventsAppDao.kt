package com.vladusecho.schoolevents.data.local

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladusecho.schoolevents.data.local.model.EventModel
import com.vladusecho.schoolevents.data.local.model.EventVoteModel
import com.vladusecho.schoolevents.data.local.model.FavouriteEventModel
import com.vladusecho.schoolevents.data.local.model.NewsModel
import com.vladusecho.schoolevents.data.local.model.NewsVoteModel
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
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        WHERE e.id = :eventId
    """)
    suspend fun getEventWithStatusById(eventId: Int, userEmail: String): EventWithStatus

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        WHERE e.isArchived = 0 AND e.status = 'APPROVED'
    """)
    fun getEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        WHERE e.creatorEmail = :creatorEmail
    """)
    fun getEventsByCreator(creatorEmail: String, userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        INNER JOIN subscribed_events s ON e.id = s.eventId
        WHERE s.userEmail = :userEmail AND e.isArchived = 0
    """)
    fun getSubscribedEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        INNER JOIN favourite_events f ON e.id = f.eventId
        WHERE f.userEmail = :userEmail
    """)
    fun getFavouriteEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
        FROM events e
        WHERE e.isArchived = 1
    """)
    fun getArchivedEvents(userEmail: String): Flow<List<EventWithStatus>>

    @Query("""
        SELECT e.*, 
        (SELECT COUNT(*) FROM favourite_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isFavourite,
        (SELECT COUNT(*) FROM subscribed_events WHERE eventId = e.id AND userEmail = :userEmail) > 0 AS isSubscribed,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM event_votes WHERE eventId = e.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM event_votes WHERE eventId = e.id AND userEmail = :userEmail) AS userVote
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

    @Query("""
        SELECT n.*,
        (SELECT COUNT(*) FROM news_votes WHERE newsId = n.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM news_votes WHERE newsId = n.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM news_votes WHERE newsId = n.id AND userEmail = :userEmail) AS userVote
        FROM news n
        ORDER BY n.id DESC
    """)
    fun getNews(userEmail: String): Flow<List<NewsWithStatus>>

    @Query("""
        SELECT n.*,
        (SELECT COUNT(*) FROM news_votes WHERE newsId = n.id AND voteType = 'LIKE') AS likes,
        (SELECT COUNT(*) FROM news_votes WHERE newsId = n.id AND voteType = 'DISLIKE') AS dislikes,
        (SELECT voteType FROM news_votes WHERE newsId = n.id AND userEmail = :userEmail) AS userVote
        FROM news n
        WHERE n.id = :newsId
    """)
    suspend fun getNewsWithStatusById(newsId: Int, userEmail: String): NewsWithStatus

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun deleteNews(newsId: Int)

    @Query("""
        SELECT p.*, s.wasAbsent FROM profile p
        INNER JOIN subscribed_events s ON p.email = s.userEmail
        WHERE s.eventId = :eventId
    """)
    fun getParticipantsWithAbsence(eventId: Int): Flow<List<ParticipantWithAbsence>>

    @Query("UPDATE subscribed_events SET wasAbsent = :wasAbsent WHERE userEmail = :userEmail AND eventId = :eventId")
    suspend fun updateAbsenceStatus(userEmail: String, eventId: Int, wasAbsent: Boolean)

    @Query("SELECT COUNT(*) FROM subscribed_events WHERE userEmail = :userEmail AND wasAbsent = 0")
    fun getAttendedEventsCount(userEmail: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM subscribed_events WHERE userEmail = :userEmail AND wasAbsent = 1")
    fun getAbsentEventsCount(userEmail: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventVote(vote: EventVoteModel)

    @Query("DELETE FROM event_votes WHERE userEmail = :userEmail AND eventId = :eventId")
    suspend fun deleteEventVote(userEmail: String, eventId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsVote(vote: NewsVoteModel)

    @Query("DELETE FROM news_votes WHERE userEmail = :userEmail AND newsId = :newsId")
    suspend fun deleteNewsVote(userEmail: String, newsId: Int)
}

data class EventWithStatus(
    @Embedded val event: EventModel,
    val isFavourite: Boolean,
    val isSubscribed: Boolean,
    val likes: Int,
    val dislikes: Int,
    val userVote: String?
)

data class NewsWithStatus(
    @Embedded val news: NewsModel,
    val likes: Int,
    val dislikes: Int,
    val userVote: String?
)

data class ParticipantWithAbsence(
    @Embedded val profile: ProfileModel,
    val wasAbsent: Boolean
)
