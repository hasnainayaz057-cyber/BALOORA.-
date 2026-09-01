package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ActivityType
import com.example.data.model.MediaType

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int?,
    val authorAvatarUrl: String?,
    val isVerified: Boolean,
    val location: String,
    val timeAgo: String,
    val caption: String,
    val hashtagsCsv: String,
    val mediaResId: Int?,
    val mediaUrl: String?,
    val mediaType: String,
    val durationSeconds: Int,
    val likesCount: Int,
    val isLiked: Boolean,
    val commentsCount: Int,
    val repostsCount: Int,
    val isReposted: Boolean,
    val isSaved: Boolean,
    val isFollowing: Boolean,
    val audioTrackTitle: String?,
    val timestamp: Long
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int?,
    val text: String,
    val timeAgo: String,
    val likesCount: Int,
    val isLiked: Boolean,
    val timestamp: Long
)

@Entity(tableName = "drafts")
data class DraftEntity(
    @PrimaryKey val id: String,
    val mediaType: String,
    val mediaResId: Int?,
    val caption: String,
    val hashtags: String,
    val mentions: String,
    val location: String,
    val timestamp: Long
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String,
    val actorName: String,
    val actorHandle: String,
    val actorAvatarRes: Int?,
    val text: String,
    val targetPostSnippet: String?,
    val targetMediaResId: Int?,
    val timeAgo: String,
    val isRead: Boolean,
    val timestamp: Long
)
