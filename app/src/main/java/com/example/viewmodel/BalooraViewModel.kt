package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.BalooraDatabase
import com.example.data.model.ActivityNotification
import com.example.data.model.Comment
import com.example.data.model.ExploreCategory
import com.example.data.model.FeedFilter
import com.example.data.model.InstagramAccount
import com.example.data.model.MediaType
import com.example.data.model.Post
import com.example.data.model.Story
import com.example.data.model.UserDraft
import com.example.data.model.UserProfile
import com.example.data.model.UserSettings
import com.example.data.repository.BalooraRepository
import com.example.ui.theme.BalooraAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    LOGIN,
    HOME,
    EXPLORE,
    CREATE,
    ACTIVITY,
    PROFILE,
    SETTINGS
}

class BalooraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BalooraRepository

    // Navigation & View States
    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _previousScreen = MutableStateFlow(AppScreen.HOME)

    // Feed Filter
    private val _selectedFeedFilter = MutableStateFlow(FeedFilter.FOR_YOU)
    val selectedFeedFilter: StateFlow<FeedFilter> = _selectedFeedFilter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Story Viewer State
    private val _activeStoryIndex = MutableStateFlow<Int?>(null)
    val activeStoryIndex: StateFlow<Int?> = _activeStoryIndex.asStateFlow()

    // Media Viewer State (Full-screen lightbox)
    private val _activeMediaPost = MutableStateFlow<Post?>(null)
    val activeMediaPost: StateFlow<Post?> = _activeMediaPost.asStateFlow()

    // Comments Sheet State
    private val _activeCommentsPost = MutableStateFlow<Post?>(null)
    val activeCommentsPost: StateFlow<Post?> = _activeCommentsPost.asStateFlow()

    private val _commentInputText = MutableStateFlow("")
    val commentInputText: StateFlow<String> = _commentInputText.asStateFlow()

    // Share Sheet State
    private val _activeSharePost = MutableStateFlow<Post?>(null)
    val activeSharePost: StateFlow<Post?> = _activeSharePost.asStateFlow()

    // Post Options Sheet State
    private val _activeOptionsPost = MutableStateFlow<Post?>(null)
    val activeOptionsPost: StateFlow<Post?> = _activeOptionsPost.asStateFlow()

    // Explore Search & Categories
    private val _exploreSearchQuery = MutableStateFlow("")
    val exploreSearchQuery: StateFlow<String> = _exploreSearchQuery.asStateFlow()

    private val _selectedExploreCategory = MutableStateFlow(ExploreCategory.TRENDING)
    val selectedExploreCategory: StateFlow<ExploreCategory> = _selectedExploreCategory.asStateFlow()

    // Create Screen States
    private val _createMediaType = MutableStateFlow(MediaType.PHOTO)
    val createMediaType: StateFlow<MediaType> = _createMediaType.asStateFlow()

    private val _createSelectedMediaRes = MutableStateFlow<Int>(R.drawable.img_baloch_mountains)
    val createSelectedMediaRes: StateFlow<Int> = _createSelectedMediaRes.asStateFlow()

    private val _createCaption = MutableStateFlow("")
    val createCaption: StateFlow<String> = _createCaption.asStateFlow()

    private val _createLocation = MutableStateFlow("Hingol National Park, Balochistan")
    val createLocation: StateFlow<String> = _createLocation.asStateFlow()

    private val _createHashtags = MutableStateFlow("#Baloora #Balochistan #Culture")
    val createHashtags: StateFlow<String> = _createHashtags.asStateFlow()

    private val _createMentions = MutableStateFlow("")
    val createMentions: StateFlow<String> = _createMentions.asStateFlow()

    private val _isPreviewMode = MutableStateFlow(false)
    val isPreviewMode: StateFlow<Boolean> = _isPreviewMode.asStateFlow()

    // Profile Tab (0 = Posts, 1 = Shorts, 2 = Saved, 3 = Reposts, 4 = Tagged)
    private val _profileSelectedTab = MutableStateFlow(0)
    val profileSelectedTab: StateFlow<Int> = _profileSelectedTab.asStateFlow()

    private val _isEditProfileDialogOpen = MutableStateFlow(false)
    val isEditProfileDialogOpen: StateFlow<Boolean> = _isEditProfileDialogOpen.asStateFlow()

    // Activity Filter (0 = All, 1 = Likes, 2 = Comments, 3 = Follows, 4 = Mentions)
    private val _activityFilterTab = MutableStateFlow(0)
    val activityFilterTab: StateFlow<Int> = _activityFilterTab.asStateFlow()

    // User message/toast snackbar state
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        val database = BalooraDatabase.getDatabase(application)
        repository = BalooraRepository(database.balooraDao())
        viewModelScope.launch {
            repository.initializeSeedDataIfEmpty()
        }
    }

    // Repository Flows
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val savedAccounts: StateFlow<List<InstagramAccount>> = repository.savedAccounts
    val userSettings: StateFlow<UserSettings> = repository.userSettings

    // Account Switcher Bottom Sheet
    private val _isAccountSwitcherOpen = MutableStateFlow(false)
    val isAccountSwitcherOpen: StateFlow<Boolean> = _isAccountSwitcherOpen.asStateFlow()
    val stories: StateFlow<List<Story>> = repository.stories
    val drafts: StateFlow<List<UserDraft>> = repository.allDrafts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rawPosts: StateFlow<List<Post>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPosts: StateFlow<List<Post>> = combine(rawPosts, _selectedFeedFilter) { posts, filter ->
        when (filter) {
            FeedFilter.FOR_YOU -> posts
            FeedFilter.FOLLOWING -> posts.filter { it.isFollowing }
            FeedFilter.LATEST -> posts.sortedByDescending { it.timestamp }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<ActivityNotification>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeComments: StateFlow<List<Comment>> = _activeCommentsPost.combine(rawPosts) { post, _ ->
        post?.id
    }.let { postIdFlow ->
        MutableStateFlow(emptyList<Comment>())
    }

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != AppScreen.SPLASH && _currentScreen.value != AppScreen.SETTINGS) {
            _previousScreen.value = _currentScreen.value
        }
        _currentScreen.value = screen
    }

    fun finishSplash() {
        _currentScreen.value = AppScreen.HOME
    }

    fun navigateBack() {
        if (_currentScreen.value == AppScreen.SETTINGS) {
            _currentScreen.value = _previousScreen.value
        } else {
            _currentScreen.value = AppScreen.HOME
        }
    }

    // Feed Actions
    fun setFeedFilter(filter: FeedFilter) {
        _selectedFeedFilter.value = filter
    }

    fun refreshFeed() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(900)
            _isRefreshing.value = false
            showToast("Feed refreshed with latest Baloora moments")
        }
    }

    fun toggleLike(post: Post) {
        viewModelScope.launch {
            repository.toggleLike(post)
        }
    }

    fun toggleSave(post: Post) {
        viewModelScope.launch {
            repository.toggleSave(post)
            showToast(if (!post.isSaved) "Saved to your private collection" else "Removed from saved")
        }
    }

    fun toggleRepost(post: Post) {
        viewModelScope.launch {
            repository.toggleRepost(post)
            showToast(if (!post.isReposted) "Reposted to your Baloora profile" else "Repost removed")
        }
    }

    fun toggleFollow(authorHandle: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFollow(authorHandle, currentStatus)
            showToast(if (!currentStatus) "Following $authorHandle" else "Unfollowed $authorHandle")
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId)
            showToast("Post deleted")
            if (_activeMediaPost.value?.id == postId) {
                _activeMediaPost.value = null
            }
        }
    }

    // Stories Viewer Actions
    fun openStory(index: Int) {
        _activeStoryIndex.value = index
        stories.value.getOrNull(index)?.let {
            repository.markStorySeen(it.id)
        }
    }

    fun closeStory() {
        _activeStoryIndex.value = null
    }

    fun nextStory() {
        val current = _activeStoryIndex.value ?: return
        if (current < stories.value.size - 1) {
            val nextIndex = current + 1
            _activeStoryIndex.value = nextIndex
            stories.value.getOrNull(nextIndex)?.let {
                repository.markStorySeen(it.id)
            }
        } else {
            closeStory()
        }
    }

    fun previousStory() {
        val current = _activeStoryIndex.value ?: return
        if (current > 0) {
            _activeStoryIndex.value = current - 1
        }
    }

    fun reactToStory(emoji: String) {
        showToast("Sent reaction $emoji to story author")
    }

    fun replyToStory(text: String) {
        if (text.isNotBlank()) {
            showToast("Reply sent: \"$text\"")
        }
    }

    // Media Lightbox Actions
    fun openMediaViewer(post: Post) {
        _activeMediaPost.value = post
    }

    fun closeMediaViewer() {
        _activeMediaPost.value = null
    }

    // Comments Sheet Actions
    fun openComments(post: Post) {
        _activeCommentsPost.value = post
        _commentInputText.value = ""
    }

    fun closeComments() {
        _activeCommentsPost.value = null
    }

    fun setCommentInputText(text: String) {
        _commentInputText.value = text
    }

    fun submitComment() {
        val text = _commentInputText.value.trim()
        val post = _activeCommentsPost.value
        if (text.isNotBlank() && post != null) {
            viewModelScope.launch {
                repository.addComment(post.id, text)
                _commentInputText.value = ""
                showToast("Comment posted!")
            }
        }
    }

    fun toggleCommentLike(comment: Comment) {
        viewModelScope.launch {
            repository.toggleCommentLike(comment)
        }
    }

    // Share Sheet Actions
    fun openShare(post: Post) {
        _activeSharePost.value = post
    }

    fun closeShare() {
        _activeSharePost.value = null
    }

    // Post Options Actions
    fun openOptions(post: Post) {
        _activeOptionsPost.value = post
    }

    fun closeOptions() {
        _activeOptionsPost.value = null
    }

    // Explore Actions
    fun setExploreSearchQuery(query: String) {
        _exploreSearchQuery.value = query
    }

    fun setExploreCategory(category: ExploreCategory) {
        _selectedExploreCategory.value = category
    }

    // Create Post Actions
    fun setCreateMediaType(type: MediaType) {
        _createMediaType.value = type
    }

    fun setCreateSelectedMediaRes(resId: Int) {
        _createSelectedMediaRes.value = resId
    }

    fun setCreateCaption(caption: String) {
        _createCaption.value = caption
    }

    fun setCreateLocation(location: String) {
        _createLocation.value = location
    }

    fun setCreateHashtags(hashtags: String) {
        _createHashtags.value = hashtags
    }

    fun setCreateMentions(mentions: String) {
        _createMentions.value = mentions
    }

    fun togglePreviewMode() {
        _isPreviewMode.value = !_isPreviewMode.value
    }

    fun publishCurrentPost() {
        val caption = _createCaption.value.trim()
        if (caption.isBlank() && _createSelectedMediaRes.value == 0) {
            showToast("Please enter a caption or select media")
            return
        }

        viewModelScope.launch {
            val tags = _createHashtags.value.split(" ", ",").filter { it.isNotBlank() }
            repository.publishPost(
                caption = caption,
                mediaType = _createMediaType.value,
                mediaResId = _createSelectedMediaRes.value,
                location = _createLocation.value,
                hashtags = tags
            )
            // Reset create state
            _createCaption.value = ""
            _createHashtags.value = "#Baloora #Balochistan #Culture"
            _createLocation.value = "Hingol National Park, Balochistan"
            _isPreviewMode.value = false
            showToast("✦ Post published to Baloora!")
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun saveCurrentDraft() {
        viewModelScope.launch {
            val draft = UserDraft(
                id = "",
                mediaType = _createMediaType.value,
                mediaResId = _createSelectedMediaRes.value,
                caption = _createCaption.value,
                hashtags = _createHashtags.value,
                mentions = _createMentions.value,
                location = _createLocation.value
            )
            repository.saveDraft(draft)
            showToast("Draft saved successfully")
        }
    }

    fun loadDraft(draft: UserDraft) {
        _createMediaType.value = draft.mediaType
        _createSelectedMediaRes.value = draft.mediaResId ?: R.drawable.img_baloch_mountains
        _createCaption.value = draft.caption
        _createHashtags.value = draft.hashtags
        _createMentions.value = draft.mentions
        _createLocation.value = draft.location
        showToast("Draft loaded")
    }

    fun deleteDraft(draftId: String) {
        viewModelScope.launch {
            repository.deleteDraft(draftId)
            showToast("Draft discarded")
        }
    }

    // Profile & Activity
    fun setProfileSelectedTab(tab: Int) {
        _profileSelectedTab.value = tab
    }

    fun openEditProfileDialog() {
        _isEditProfileDialogOpen.value = true
    }

    fun closeEditProfileDialog() {
        _isEditProfileDialogOpen.value = false
    }

    fun updateProfile(displayName: String, bio: String, location: String) {
        repository.updateProfile(displayName, bio, location)
        closeEditProfileDialog()
        showToast("Profile updated")
    }

    fun setActivityFilterTab(tab: Int) {
        _activityFilterTab.value = tab
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            showToast("All notifications marked as read")
        }
    }

    // Settings Updates
    fun updateSettings(newSettings: UserSettings) {
        repository.updateSettings(newSettings)
    }

    fun setAccent(accent: BalooraAccent) {
        val current = userSettings.value
        repository.updateSettings(current.copy(accentName = accent.name))
        showToast("Accent updated to ${accent.displayName}")
    }

    fun clearCache() {
        val reclaimed = repository.clearCache()
        showToast(String.format("Cleared %.1f MB of cache & temporary media", reclaimed))
    }

    fun loginWithInstagram(handle: String, displayName: String? = null, bio: String? = null) {
        val account = repository.loginWithInstagram(handle, displayName, bio)
        _currentScreen.value = AppScreen.HOME
        showToast("Logged in as @${account.username}")
    }

    fun switchAccount(username: String) {
        repository.switchInstagramAccount(username)
        _isAccountSwitcherOpen.value = false
        showToast("Switched to @${username.removePrefix("@")}")
    }

    fun openAccountSwitcher() {
        _isAccountSwitcherOpen.value = true
    }

    fun closeAccountSwitcher() {
        _isAccountSwitcherOpen.value = false
    }

    fun logout() {
        repository.logoutInstagram()
        _isAccountSwitcherOpen.value = false
        _currentScreen.value = AppScreen.LOGIN
        showToast("Logged out of Instagram")
    }

    fun showToast(message: String) {
        _userMessage.value = message
    }

    fun clearToast() {
        _userMessage.value = null
    }
}
