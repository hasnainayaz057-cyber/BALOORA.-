package com.example.data.repository

import com.example.R
import com.example.data.local.BalooraDao
import com.example.data.local.CommentEntity
import com.example.data.local.DraftEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.PostEntity
import com.example.data.model.ActivityNotification
import com.example.data.model.ActivityType
import com.example.data.model.Comment
import com.example.data.model.InstagramAccount
import com.example.data.model.MediaType
import com.example.data.model.Post
import com.example.data.model.Story
import com.example.data.model.UserDraft
import com.example.data.model.UserProfile
import com.example.data.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class BalooraRepository(private val dao: BalooraDao) {

    private val _savedAccounts = MutableStateFlow(
        listOf(
            InstagramAccount(
                username = "raskolnikov_h1",
                displayName = "Hasnain Ayaz",
                bio = "Architect of Baloora ✦ Exploring digital heritage, contemporary minimalism & Baloch landscapes ⛰️ Crafted with pride.",
                location = "Gwadar & Quetta",
                avatarRes = R.drawable.img_creator_avatar,
                followersCount = 28400,
                followingCount = 412,
                postsCount = 38,
                isVerified = true,
                culturalTitle = "البلوشی Design Lab"
            ),
            InstagramAccount(
                username = "kahoodaayaz55",
                displayName = "Kahooda Ayaz",
                bio = "Digital Explorer & Sovereign Creator ✦ Baloora Instagram Network ✨",
                location = "Makran Coast, Balochistan",
                avatarRes = R.drawable.img_baloch_desert,
                followersCount = 14800,
                followingCount = 295,
                postsCount = 19,
                isVerified = false,
                culturalTitle = "Digital Explorer"
            ),
            InstagramAccount(
                username = "chagai_heritage",
                displayName = "Chagai Heritage Lab",
                bio = "Balochistan desert geology, ancient inscriptions & cultural architecture 🏺",
                location = "Chagai & Nushki",
                avatarRes = R.drawable.img_baloch_mountains,
                followersCount = 45200,
                followingCount = 180,
                postsCount = 64,
                isVerified = true,
                culturalTitle = "Heritage Archive"
            )
        )
    )
    val savedAccounts: StateFlow<List<InstagramAccount>> = _savedAccounts.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            username = "raskolnikov_h1",
            displayName = "Hasnain Ayaz",
            bio = "Architect of Baloora ✦ Exploring digital heritage, contemporary minimalism & Baloch landscapes ⛰️ Crafted with pride.",
            location = "Gwadar & Quetta",
            instagramHandle = "raskolnikov_h1",
            avatarRes = R.drawable.img_creator_avatar,
            followersCount = 28400,
            followingCount = 412,
            postsCount = 38,
            isVerified = true,
            culturalTitle = "البلوشی Design Lab",
            isLoggedIn = true
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    private val _stories = MutableStateFlow(getInitialStories())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    val allPosts: Flow<List<Post>> = dao.getAllPosts().map { entities ->
        entities.map { it.toDomainModel() }
    }

    val allDrafts: Flow<List<UserDraft>> = dao.getAllDrafts().map { entities ->
        entities.map { it.toDomainModel() }
    }

    val allNotifications: Flow<List<ActivityNotification>> = dao.getAllNotifications().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun initializeSeedDataIfEmpty() {
        val seedPosts = getInitialPosts()
        dao.insertPosts(seedPosts.map { it.toEntity() })

        val seedNotifications = getInitialNotifications()
        dao.insertNotifications(seedNotifications.map { it.toEntity() })

        val seedComments = getInitialComments()
        for (c in seedComments) {
            dao.insertComment(c.toEntity())
        }
    }

    suspend fun toggleLike(post: Post) {
        val newLikedState = !post.isLiked
        val newCount = if (newLikedState) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
        dao.updatePostLike(post.id, newLikedState, newCount)

        if (newLikedState) {
            val notif = NotificationEntity(
                id = UUID.randomUUID().toString(),
                type = ActivityType.LIKE.name,
                actorName = "You",
                actorHandle = "@hasnain_ayaz",
                actorAvatarRes = R.drawable.img_creator_avatar,
                text = "liked a post in your feed.",
                targetPostSnippet = post.caption.take(30),
                targetMediaResId = post.mediaResId,
                timeAgo = "Just now",
                isRead = false,
                timestamp = System.currentTimeMillis()
            )
            dao.insertNotification(notif)
        }
    }

    suspend fun toggleSave(post: Post) {
        dao.updatePostSaved(post.id, !post.isSaved)
    }

    suspend fun toggleRepost(post: Post) {
        val newRepostState = !post.isReposted
        val newCount = if (newRepostState) post.repostsCount + 1 else maxOf(0, post.repostsCount - 1)
        dao.updatePostRepost(post.id, newRepostState, newCount)
    }

    suspend fun toggleFollow(authorHandle: String, currentStatus: Boolean) {
        dao.updateAuthorFollowing(authorHandle, !currentStatus)
    }

    suspend fun deletePost(postId: String) {
        dao.deletePost(postId)
    }

    fun getCommentsForPost(postId: String): Flow<List<Comment>> {
        return dao.getCommentsForPost(postId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun addComment(postId: String, text: String) {
        val newComment = CommentEntity(
            id = UUID.randomUUID().toString(),
            postId = postId,
            authorName = _userProfile.value.displayName,
            authorHandle = "@" + _userProfile.value.username,
            authorAvatarRes = R.drawable.img_creator_avatar,
            text = text,
            timeAgo = "Just now",
            likesCount = 0,
            isLiked = false,
            timestamp = System.currentTimeMillis()
        )
        dao.insertComment(newComment)
    }

    suspend fun toggleCommentLike(comment: Comment) {
        val newLikedState = !comment.isLiked
        val newCount = if (newLikedState) comment.likesCount + 1 else maxOf(0, comment.likesCount - 1)
        dao.updateCommentLike(comment.id, newLikedState, newCount)
    }

    fun markStorySeen(storyId: String) {
        _stories.value = _stories.value.map {
            if (it.id == storyId) it.copy(isSeen = true) else it
        }
    }

    fun addStory(story: Story) {
        _stories.value = listOf(story) + _stories.value
    }

    suspend fun publishPost(
        caption: String,
        mediaType: MediaType,
        mediaResId: Int?,
        location: String,
        hashtags: List<String>,
        audioTrackTitle: String? = null
    ) {
        val post = PostEntity(
            id = UUID.randomUUID().toString(),
            authorName = _userProfile.value.displayName,
            authorHandle = "@" + _userProfile.value.username,
            authorAvatarRes = R.drawable.img_creator_avatar,
            authorAvatarUrl = null,
            isVerified = true,
            location = location.ifBlank { "Gwadar, Balochistan" },
            timeAgo = "Just now",
            caption = caption,
            hashtagsCsv = hashtags.joinToString(","),
            mediaResId = mediaResId ?: R.drawable.img_baloch_mountains,
            mediaUrl = null,
            mediaType = mediaType.name,
            durationSeconds = if (mediaType == MediaType.SHORT || mediaType == MediaType.VIDEO) 15 else 0,
            likesCount = 0,
            isLiked = false,
            commentsCount = 0,
            repostsCount = 0,
            isReposted = false,
            isSaved = false,
            isFollowing = true,
            audioTrackTitle = audioTrackTitle,
            timestamp = System.currentTimeMillis()
        )
        dao.insertPost(post)
    }

    suspend fun saveDraft(draft: UserDraft) {
        dao.insertDraft(
            DraftEntity(
                id = draft.id.ifBlank { UUID.randomUUID().toString() },
                mediaType = draft.mediaType.name,
                mediaResId = draft.mediaResId,
                caption = draft.caption,
                hashtags = draft.hashtags,
                mentions = draft.mentions,
                location = draft.location,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteDraft(draftId: String) {
        dao.deleteDraft(draftId)
    }

    suspend fun markNotificationAsRead(id: String) {
        dao.markNotificationAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        dao.markAllNotificationsAsRead()
    }

    fun updateSettings(newSettings: UserSettings) {
        _userSettings.value = newSettings
    }

    fun loginWithInstagram(
        inputHandle: String,
        customDisplayName: String? = null,
        customBio: String? = null
    ): InstagramAccount {
        val cleanHandle = inputHandle.trim().removePrefix("@").lowercase()
        val existing = _savedAccounts.value.find { it.username.equals(cleanHandle, ignoreCase = true) }
        
        val accountToUse = if (existing != null) {
            existing
        } else {
            val formattedName = customDisplayName?.ifBlank { null } 
                ?: cleanHandle.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            val newAcc = InstagramAccount(
                username = cleanHandle,
                displayName = formattedName,
                bio = customBio?.ifBlank { null } ?: "Creator on Instagram & Baloora ✨",
                location = "Gwadar & Quetta",
                avatarRes = R.drawable.img_creator_avatar,
                followersCount = (1200..45000).random(),
                followingCount = (150..600).random(),
                postsCount = (5..35).random(),
                isVerified = cleanHandle.contains("official") || cleanHandle == "raskolnikov_h1",
                culturalTitle = "Instagram Creator"
            )
            _savedAccounts.value = _savedAccounts.value + newAcc
            newAcc
        }

        _userProfile.value = UserProfile(
            username = accountToUse.username,
            displayName = accountToUse.displayName,
            bio = accountToUse.bio,
            location = accountToUse.location,
            instagramHandle = accountToUse.username,
            avatarRes = accountToUse.avatarRes ?: R.drawable.img_creator_avatar,
            followersCount = accountToUse.followersCount,
            followingCount = accountToUse.followingCount,
            postsCount = accountToUse.postsCount,
            isVerified = accountToUse.isVerified,
            culturalTitle = accountToUse.culturalTitle,
            isLoggedIn = true
        )
        return accountToUse
    }

    fun switchInstagramAccount(username: String) {
        val clean = username.trim().removePrefix("@").lowercase()
        val target = _savedAccounts.value.find { it.username.equals(clean, ignoreCase = true) }
        if (target != null) {
            _userProfile.value = UserProfile(
                username = target.username,
                displayName = target.displayName,
                bio = target.bio,
                location = target.location,
                instagramHandle = target.username,
                avatarRes = target.avatarRes ?: R.drawable.img_creator_avatar,
                followersCount = target.followersCount,
                followingCount = target.followingCount,
                postsCount = target.postsCount,
                isVerified = target.isVerified,
                culturalTitle = target.culturalTitle,
                isLoggedIn = true
            )
        }
    }

    fun logoutInstagram() {
        _userProfile.value = _userProfile.value.copy(isLoggedIn = false)
    }

    fun updateProfile(displayName: String, bio: String, location: String) {
        val updatedProfile = _userProfile.value.copy(
            displayName = displayName,
            bio = bio,
            location = location
        )
        _userProfile.value = updatedProfile

        // Keep savedAccounts in sync
        _savedAccounts.value = _savedAccounts.value.map {
            if (it.username.equals(updatedProfile.username, ignoreCase = true)) {
                it.copy(displayName = displayName, bio = bio, location = location)
            } else it
        }
    }

    fun clearCache(): Float {
        val reclaimed = _userSettings.value.cachedDataMb
        _userSettings.value = _userSettings.value.copy(cachedDataMb = 0.4f)
        return reclaimed
    }

    // Converters
    private fun PostEntity.toDomainModel() = Post(
        id = id,
        authorName = authorName,
        authorHandle = authorHandle,
        authorAvatarRes = authorAvatarRes,
        authorAvatarUrl = authorAvatarUrl,
        isVerified = isVerified,
        location = location,
        timeAgo = timeAgo,
        caption = caption,
        hashtags = if (hashtagsCsv.isBlank()) emptyList() else hashtagsCsv.split(","),
        mediaResId = mediaResId,
        mediaUrl = mediaUrl,
        mediaType = runCatching { MediaType.valueOf(mediaType) }.getOrDefault(MediaType.PHOTO),
        durationSeconds = durationSeconds,
        likesCount = likesCount,
        isLiked = isLiked,
        commentsCount = commentsCount,
        repostsCount = repostsCount,
        isReposted = isReposted,
        isSaved = isSaved,
        isFollowing = isFollowing,
        audioTrackTitle = audioTrackTitle,
        timestamp = timestamp
    )

    private fun Post.toEntity() = PostEntity(
        id = id,
        authorName = authorName,
        authorHandle = authorHandle,
        authorAvatarRes = authorAvatarRes,
        authorAvatarUrl = authorAvatarUrl,
        isVerified = isVerified,
        location = location,
        timeAgo = timeAgo,
        caption = caption,
        hashtagsCsv = hashtags.joinToString(","),
        mediaResId = mediaResId,
        mediaUrl = mediaUrl,
        mediaType = mediaType.name,
        durationSeconds = durationSeconds,
        likesCount = likesCount,
        isLiked = isLiked,
        commentsCount = commentsCount,
        repostsCount = repostsCount,
        isReposted = isReposted,
        isSaved = isSaved,
        isFollowing = isFollowing,
        audioTrackTitle = audioTrackTitle,
        timestamp = timestamp
    )

    private fun CommentEntity.toDomainModel() = Comment(
        id = id,
        postId = postId,
        authorName = authorName,
        authorHandle = authorHandle,
        authorAvatarRes = authorAvatarRes,
        text = text,
        timeAgo = timeAgo,
        likesCount = likesCount,
        isLiked = isLiked
    )

    private fun Comment.toEntity() = CommentEntity(
        id = id,
        postId = postId,
        authorName = authorName,
        authorHandle = authorHandle,
        authorAvatarRes = authorAvatarRes,
        text = text,
        timeAgo = timeAgo,
        likesCount = likesCount,
        isLiked = isLiked,
        timestamp = System.currentTimeMillis()
    )

    private fun DraftEntity.toDomainModel() = UserDraft(
        id = id,
        mediaType = runCatching { MediaType.valueOf(mediaType) }.getOrDefault(MediaType.PHOTO),
        mediaResId = mediaResId,
        caption = caption,
        hashtags = hashtags,
        mentions = mentions,
        location = location,
        timestamp = timestamp
    )

    private fun NotificationEntity.toDomainModel() = ActivityNotification(
        id = id,
        type = runCatching { ActivityType.valueOf(type) }.getOrDefault(ActivityType.LIKE),
        actorName = actorName,
        actorHandle = actorHandle,
        actorAvatarRes = actorAvatarRes,
        text = text,
        targetPostSnippet = targetPostSnippet,
        targetMediaResId = targetMediaResId,
        timeAgo = timeAgo,
        isRead = isRead,
        timestamp = timestamp
    )

    private fun ActivityNotification.toEntity() = NotificationEntity(
        id = id,
        type = type.name,
        actorName = actorName,
        actorHandle = actorHandle,
        actorAvatarRes = actorAvatarRes,
        text = text,
        targetPostSnippet = targetPostSnippet,
        targetMediaResId = targetMediaResId,
        timeAgo = timeAgo,
        isRead = isRead,
        timestamp = timestamp
    )

    private fun getInitialStories(): List<Story> = listOf(
        Story(
            id = "story-1",
            authorName = "Hasnain Ayaz",
            authorHandle = "@hasnain_ayaz",
            authorAvatarRes = R.drawable.img_creator_avatar,
            mediaResId = R.drawable.img_baloch_mountains,
            caption = "Golden twilight over Makran mountain ranges. Serenity in Balochistan 🌅",
            isSeen = false,
            durationSeconds = 6,
            timeAgo = "45m ago",
            location = "Makran Coastal Range"
        ),
        Story(
            id = "story-2",
            authorName = "Zarwan Studio",
            authorHandle = "@zarwan_art",
            authorAvatarRes = R.drawable.img_baloora_icon,
            mediaResId = R.drawable.img_baloch_desert,
            caption = "Where desert dunes merge seamlessly with azure waters 🌊",
            isSeen = false,
            durationSeconds = 5,
            timeAgo = "2h ago",
            location = "Gwadar Beach"
        ),
        Story(
            id = "story-3",
            authorName = "Maryam Baloch",
            authorHandle = "@maryam_b",
            authorAvatarRes = R.drawable.img_creator_avatar,
            mediaResId = R.drawable.img_baloch_mountains,
            caption = "Capturing geometric motifs woven into modern architectural designs ✨",
            isSeen = true,
            durationSeconds = 7,
            timeAgo = "5h ago",
            location = "Quetta Valley"
        )
    )

    private fun getInitialPosts(): List<Post> = listOf(
        Post(
            id = "post-1",
            authorName = "Hasnain Ayaz",
            authorHandle = "@hasnain_ayaz",
            authorAvatarRes = R.drawable.img_creator_avatar,
            isVerified = true,
            location = "Hingol Canyon, Balochistan",
            timeAgo = "1h ago",
            caption = "Standing before the eternal monoliths of Balochistan. Ancient geology meeting futuristic design aesthetics. ⛰️✨ #Baloora #Balochistan #Heritage #Architecture",
            hashtags = listOf("#Baloora", "#Balochistan", "#Heritage", "#Architecture"),
            mediaResId = R.drawable.img_baloch_mountains,
            mediaType = MediaType.PHOTO,
            likesCount = 1842,
            isLiked = true,
            commentsCount = 142,
            repostsCount = 89,
            isReposted = false,
            isSaved = true,
            isFollowing = true,
            audioTrackTitle = "Zarwan Ambient • Baloch Chhapa"
        ),
        Post(
            id = "post-2",
            authorName = "Baloora Cultural Lab",
            authorHandle = "@baloora_official",
            authorAvatarRes = R.drawable.img_baloora_icon,
            isVerified = true,
            location = "Gwadar Hammerhead Coastline",
            timeAgo = "4h ago",
            caption = "A new paradigm in social connection. Minimalist lines, golden sands, and sovereign digital identity. Powered by البلوشی. 🦅⚡ #BalooraLaunch #NextGen #ModernCulture",
            hashtags = listOf("#BalooraLaunch", "#NextGen", "#ModernCulture"),
            mediaResId = R.drawable.img_baloch_desert,
            mediaType = MediaType.SHORT,
            durationSeconds = 24,
            likesCount = 3420,
            isLiked = false,
            commentsCount = 286,
            repostsCount = 310,
            isReposted = false,
            isSaved = false,
            isFollowing = true,
            audioTrackTitle = "Desert Winds & Synth Harmonics"
        ),
        Post(
            id = "post-3",
            authorName = "Farooq Rind",
            authorHandle = "@farooq_rind",
            authorAvatarRes = R.drawable.img_creator_avatar,
            isVerified = false,
            location = "Koh-e-Suleman Heights",
            timeAgo = "6h ago",
            caption = "Dusk falls upon the mountain spine. Clear skies, starlight, and absolute clarity of mind. 🌌📸",
            hashtags = listOf("#Balochistan", "#Nature", "#Stargazing"),
            mediaResId = R.drawable.img_baloch_mountains,
            mediaType = MediaType.VIDEO,
            durationSeconds = 45,
            likesCount = 920,
            isLiked = false,
            commentsCount = 58,
            repostsCount = 24,
            isReposted = false,
            isSaved = false,
            isFollowing = false,
            audioTrackTitle = "Suleman String Ensemble"
        )
    )

    private fun getInitialComments(): List<Comment> = listOf(
        Comment(
            id = "comment-1",
            postId = "post-1",
            authorName = "Mir Zafar",
            authorHandle = "@mir_zafar",
            authorAvatarRes = R.drawable.img_creator_avatar,
            text = "The lighting and depth here are spectacular! Truly reflects the soul of the land.",
            timeAgo = "45m ago",
            likesCount = 24,
            isLiked = true
        ),
        Comment(
            id = "comment-2",
            postId = "post-1",
            authorName = "Shirin K.",
            authorHandle = "@shirin_design",
            authorAvatarRes = R.drawable.img_baloora_icon,
            text = "The typography and minimal palette in Baloora are next level. Bravo Hasnain! 👏",
            timeAgo = "20m ago",
            likesCount = 18,
            isLiked = false
        )
    )

    private fun getInitialNotifications(): List<ActivityNotification> = listOf(
        ActivityNotification(
            id = "notif-1",
            type = ActivityType.LIKE,
            actorName = "Maryam Baloch and 4 others",
            actorHandle = "@maryam_b",
            actorAvatarRes = R.drawable.img_creator_avatar,
            text = "liked your post 'Standing before the eternal monoliths...'",
            targetPostSnippet = "Standing before the eternal monoliths...",
            targetMediaResId = R.drawable.img_baloch_mountains,
            timeAgo = "12m ago",
            isRead = false
        ),
        ActivityNotification(
            id = "notif-2",
            type = ActivityType.FOLLOW,
            actorName = "Zarwan Studio",
            actorHandle = "@zarwan_art",
            actorAvatarRes = R.drawable.img_baloora_icon,
            text = "started following your creative journey.",
            targetPostSnippet = null,
            targetMediaResId = null,
            timeAgo = "1h ago",
            isRead = false
        ),
        ActivityNotification(
            id = "notif-3",
            type = ActivityType.MENTION,
            actorName = "Shirin K.",
            actorHandle = "@shirin_design",
            actorAvatarRes = R.drawable.img_baloora_icon,
            text = "mentioned you in a comment: 'Bravo Hasnain! 👏'",
            targetPostSnippet = "Bravo Hasnain! 👏",
            targetMediaResId = R.drawable.img_baloch_desert,
            timeAgo = "3h ago",
            isRead = true
        ),
        ActivityNotification(
            id = "notif-4",
            type = ActivityType.REPOST,
            actorName = "Farooq Rind",
            actorHandle = "@farooq_rind",
            actorAvatarRes = R.drawable.img_creator_avatar,
            text = "reposted your story to their profile.",
            targetPostSnippet = null,
            targetMediaResId = R.drawable.img_baloch_mountains,
            timeAgo = "5h ago",
            isRead = true
        )
    )
}
