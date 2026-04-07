package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.LeaderboardEntryEntity
import com.m1x.gymmer.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("SELECT * FROM leaderboard ORDER BY rank ASC")
    fun getLeaderboard(): Flow<List<LeaderboardEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntry(entry: LeaderboardEntryEntity)
}
