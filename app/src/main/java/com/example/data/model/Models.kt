package com.example.data.model

enum class MediaType {
    PHOTO,
    VIDEO,
    SHORT,
    CAROUSEL
}

enum class FeedFilter(val label: String) {
    FOR_YOU("For You"),
    FOLLOWING("Following"),
    LATEST("Latest")
}

enum class ExploreCategory(val label: String, val tag: String) {
    TRENDING("🔥 Trending", "#trending"),
    BALOCH_HERITAGE("🏔️ Balochistan", "#balochistan"),
    CREATORS("✨ Top Creators", "#creators"),
    LANDSCAPES("🌅 Landscapes", "#landscapes"),
    SHORTS("⚡ Shorts", "#shorts"),
    CULTURE("🎨 Art & Culture", "#culture"),
    MUSIC("🎵 Folk & Ambient", "#ambient")
}

enum class FeedDensity(val label: String) {
    COMPACT("Compact"),
    COZY("Cozy (Default)"),
    SPACIOUS("Spacious / Immersive")
}

enum class AnimationSpeed(val label: String, val factor: Float) {
    SNAPPY("Snappy (0.5x)", 0.5f),
    BALANCED("Balanced (1.0x)", 1.0f),
    CINEMATIC("Cinematic (1.5x)", 1.5f)
}

data class Post(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int? = null,
    val authorAvatarUrl: String? = null,
    val isVerified: Boolean = false,
    val location: String = "",
    val timeAgo: String = "2h ago",
    val caption: String = "",
    val hashtags: List<String> = emptyList(),
    val mediaResId: Int? = null,
    val mediaUrl: String? = null,
    val mediaType: MediaType = MediaType.PHOTO,
    val durationSeconds: Int = 0,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0,
    val repostsCount: Int = 0,
    val isReposted: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowing: Boolean = false,
    val audioTrackTitle: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class Story(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int? = null,
    val mediaResId: Int? = null,
    val mediaUrl: String? = null,
    val caption: String = "",
    val isSeen: Boolean = false,
    val durationSeconds: Int = 5,
    val timeAgo: String = "1h ago",
    val location: String = ""
)

data class Comment(
    val id: String,
    val postId: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int? = null,
    val text: String,
    val timeAgo: String = "Just now",
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

enum class ActivityType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION,
    REPOST,
    STORY_REPLY
}

data class ActivityNotification(
    val id: String,
    val type: ActivityType,
    val actorName: String,
    val actorHandle: String,
    val actorAvatarRes: Int? = null,
    val text: String,
    val targetPostId: String? = null,
    val targetPostSnippet: String? = null,
    val targetMediaResId: Int? = null,
    val timeAgo: String = "10m ago",
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserDraft(
    val id: String,
    val mediaType: MediaType = MediaType.PHOTO,
    val mediaResId: Int? = null,
    val caption: String = "",
    val hashtags: String = "",
    val mentions: String = "",
    val location: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class InstagramAccount(
    val username: String,
    val displayName: String,
    val bio: String,
    val location: String = "Gwadar & Quetta",
    val avatarRes: Int? = null,
    val followersCount: Int = 12400,
    val followingCount: Int = 380,
    val postsCount: Int = 24,
    val isVerified: Boolean = false,
    val culturalTitle: String = "Creator"
)

data class UserProfile(
    val username: String = "raskolnikov_h1",
    val displayName: String = "Hasnain Ayaz",
    val bio: String = "Architect of Baloora ✦ Exploring digital heritage, contemporary minimalism & Baloch landscapes ⛰️ Crafted with pride.",
    val location: String = "Gwadar & Quetta",
    val instagramHandle: String = "raskolnikov_h1",
    val avatarRes: Int? = null,
    val followersCount: Int = 28400,
    val followingCount: Int = 412,
    val postsCount: Int = 38,
    val isVerified: Boolean = true,
    val culturalTitle: String = "البلوشی Design Lab",
    val isLoggedIn: Boolean = true
)

data class UserSettings(
    val isDarkMode: Boolean = true,
    val isOledBlack: Boolean = false,
    val accentName: String = "ZARWAN_GOLD",
    val feedDensity: FeedDensity = FeedDensity.COZY,
    val fontScale: Float = 1.0f,
    val animationSpeed: AnimationSpeed = AnimationSpeed.BALANCED,
    val isPrivateAccount: Boolean = false,
    val allowStorySharing: Boolean = true,
    val showActivityStatus: Boolean = true,
    val ghostMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val notifyLikes: Boolean = true,
    val notifyComments: Boolean = true,
    val notifyFollows: Boolean = true,
    val notifyMentions: Boolean = true,
    val highQualityUploads: Boolean = true,
    val dataSaverMode: Boolean = false,
    val cachedDataMb: Float = 34.8f
)
