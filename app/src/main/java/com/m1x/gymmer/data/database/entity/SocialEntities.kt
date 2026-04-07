package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val content: String,
    val imageUrl: String?,
    val timestamp: Long,
    val likesCount: Int,
    val commentsCount: Int
)

@Entity(tableName = "leaderboard")
data class LeaderboardEntryEntity(
    @PrimaryKey val userId: String,
    val userName: String,
    val rank: Int,
    val points: Int,
    val achievementBadgeUrl: String?
)
