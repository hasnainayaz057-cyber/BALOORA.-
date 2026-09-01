package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.ActivityType
import com.example.ui.components.AccountSwitcherBottomSheet
import com.example.ui.components.BalooraBottomNav
import com.example.ui.components.BalooraTopBar
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.PostOptionsMenu
import com.example.ui.components.ShareModalSheet
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.CreatePostScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InstagramLoginScreen
import com.example.ui.screens.MediaViewerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StoryViewerScreen
import com.example.ui.theme.BalooraAccent
import com.example.ui.theme.BalooraTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BalooraViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BalooraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userSettings by viewModel.userSettings.collectAsState()
            val accent = BalooraAccent.fromName(userSettings.accentName)

            BalooraTheme(
                darkTheme = userSettings.isDarkMode,
                accent = accent,
                isOledBlack = userSettings.isOledBlack,
                fontScale = userSettings.fontScale
            ) {
                BalooraApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BalooraApp(viewModel: BalooraViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val feedPosts by viewModel.filteredPosts.collectAsState()
    val rawPosts by viewModel.rawPosts.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val drafts by viewModel.drafts.collectAsState()

    val selectedFilter by viewModel.selectedFeedFilter.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val activeStoryIndex by viewModel.activeStoryIndex.collectAsState()
    val activeMediaPost by viewModel.activeMediaPost.collectAsState()
    val activeCommentsPost by viewModel.activeCommentsPost.collectAsState()
    val commentInputText by viewModel.commentInputText.collectAsState()
    val activeSharePost by viewModel.activeSharePost.collectAsState()
    val activeOptionsPost by viewModel.activeOptionsPost.collectAsState()

    val exploreSearchQuery by viewModel.exploreSearchQuery.collectAsState()
    val selectedExploreCategory by viewModel.selectedExploreCategory.collectAsState()

    val createMediaType by viewModel.createMediaType.collectAsState()
    val createSelectedMediaRes by viewModel.createSelectedMediaRes.collectAsState()
    val createCaption by viewModel.createCaption.collectAsState()
    val createLocation by viewModel.createLocation.collectAsState()
    val createHashtags by viewModel.createHashtags.collectAsState()
    val createMentions by viewModel.createMentions.collectAsState()
    val isPreviewMode by viewModel.isPreviewMode.collectAsState()

    val profileSelectedTab by viewModel.profileSelectedTab.collectAsState()
    val isEditProfileDialogOpen by viewModel.isEditProfileDialogOpen.collectAsState()
    val activityFilterTab by viewModel.activityFilterTab.collectAsState()

    val savedAccounts by viewModel.savedAccounts.collectAsState()
    val isAccountSwitcherOpen by viewModel.isAccountSwitcherOpen.collectAsState()

    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val unreadNotificationsCount = notifications.count { !it.isRead }

    // Handle Toast snackbar messages
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg)
            viewModel.clearToast()
        }
    }

    // Hardware back handler
    BackHandler(enabled = currentScreen != AppScreen.SPLASH) {
        if (activeStoryIndex != null) {
            viewModel.closeStory()
        } else if (activeMediaPost != null) {
            viewModel.closeMediaViewer()
        } else if (activeCommentsPost != null) {
            viewModel.closeComments()
        } else if (activeSharePost != null) {
            viewModel.closeShare()
        } else if (activeOptionsPost != null) {
            viewModel.closeOptions()
        } else if (currentScreen != AppScreen.HOME) {
            viewModel.navigateBack()
        }
    }

    // If Splash screen is active, show full-screen animated splash
    if (currentScreen == AppScreen.SPLASH) {
        SplashScreen(
            onSplashFinished = { viewModel.finishSplash() },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // If Login screen is active, show full-screen Instagram authentication
    if (currentScreen == AppScreen.LOGIN) {
        InstagramLoginScreen(
            savedAccounts = savedAccounts,
            onLoginSuccess = { handle, displayName, bio ->
                viewModel.loginWithInstagram(handle, displayName, bio)
            },
            onSkipToGuest = { viewModel.navigateTo(AppScreen.HOME) },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("baloora_main_scaffold"),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentScreen != AppScreen.SETTINGS && currentScreen != AppScreen.LOGIN) {
                BalooraTopBar(
                    currentScreen = currentScreen,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setFeedFilter(it) },
                    onDirectMessageClick = { viewModel.showToast("Direct Messages: Connected to sovereign mesh") },
                    onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    unreadNotificationsCount = unreadNotificationsCount
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.SETTINGS && currentScreen != AppScreen.LOGIN) {
                BalooraBottomNav(
                    currentScreen = currentScreen,
                    onItemSelected = { viewModel.navigateTo(it) },
                    unreadActivityCount = unreadNotificationsCount
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(220),
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.SPLASH, AppScreen.LOGIN -> {
                        // Handled above
                    }
                    AppScreen.HOME -> {
                        HomeScreen(
                            stories = stories,
                            posts = feedPosts,
                            feedDensity = userSettings.feedDensity,
                            isRefreshing = isRefreshing,
                            onRefresh = { viewModel.refreshFeed() },
                            onStoryClick = { viewModel.openStory(it) },
                            onAddStoryClick = {
                                viewModel.setCreateMediaType(com.example.data.model.MediaType.PHOTO)
                                viewModel.navigateTo(AppScreen.CREATE)
                            },
                            onLikeClick = { viewModel.toggleLike(it) },
                            onCommentClick = { viewModel.openComments(it) },
                            onShareClick = { viewModel.openShare(it) },
                            onRepostClick = { viewModel.toggleRepost(it) },
                            onSaveClick = { viewModel.toggleSave(it) },
                            onOptionsClick = { viewModel.openOptions(it) },
                            onMediaClick = { viewModel.openMediaViewer(it) },
                            onAuthorClick = { viewModel.navigateTo(AppScreen.PROFILE) }
                        )
                    }
                    AppScreen.EXPLORE -> {
                        ExploreScreen(
                            searchQuery = exploreSearchQuery,
                            onSearchQueryChange = { viewModel.setExploreSearchQuery(it) },
                            selectedCategory = selectedExploreCategory,
                            onSelectCategory = { viewModel.setExploreCategory(it) },
                            posts = rawPosts,
                            onPostClick = { viewModel.openMediaViewer(it) },
                            onToggleFollow = { handle, currentStatus ->
                                viewModel.toggleFollow(handle, currentStatus)
                            }
                        )
                    }
                    AppScreen.CREATE -> {
                        CreatePostScreen(
                            mediaType = createMediaType,
                            onMediaTypeChange = { viewModel.setCreateMediaType(it) },
                            selectedMediaRes = createSelectedMediaRes,
                            onSelectMediaRes = { viewModel.setCreateSelectedMediaRes(it) },
                            caption = createCaption,
                            onCaptionChange = { viewModel.setCreateCaption(it) },
                            location = createLocation,
                            onLocationChange = { viewModel.setCreateLocation(it) },
                            hashtags = createHashtags,
                            onHashtagsChange = { viewModel.setCreateHashtags(it) },
                            mentions = createMentions,
                            onMentionsChange = { viewModel.setCreateMentions(it) },
                            isPreviewMode = isPreviewMode,
                            onTogglePreview = { viewModel.togglePreviewMode() },
                            onPublish = { viewModel.publishCurrentPost() },
                            onSaveDraft = { viewModel.saveCurrentDraft() },
                            drafts = drafts,
                            onLoadDraft = { viewModel.loadDraft(it) },
                            onDeleteDraft = { viewModel.deleteDraft(it) }
                        )
                    }
                    AppScreen.ACTIVITY -> {
                        ActivityScreen(
                            notifications = notifications,
                            selectedFilterTab = activityFilterTab,
                            onSelectFilterTab = { viewModel.setActivityFilterTab(it) },
                            onNotificationClick = { notif ->
                                viewModel.markNotificationAsRead(notif.id)
                                rawPosts.find { it.id == notif.targetPostId }?.let { post ->
                                    viewModel.openMediaViewer(post)
                                }
                            },
                            onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
                            onFollowBack = { handle ->
                                viewModel.toggleFollow(handle, false)
                            }
                        )
                    }
                    AppScreen.PROFILE -> {
                        ProfileScreen(
                            userProfile = userProfile,
                            userPosts = rawPosts.filter {
                                it.authorHandle.removePrefix("@").equals(userProfile.instagramHandle.removePrefix("@"), ignoreCase = true)
                                || it.authorHandle == "@hasnain_ayaz"
                                || it.authorHandle == "@raskolnikov_h1"
                            },
                            selectedTab = profileSelectedTab,
                            onSelectTab = { viewModel.setProfileSelectedTab(it) },
                            onEditProfileClick = { viewModel.openEditProfileDialog() },
                            onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                            onPostClick = { viewModel.openMediaViewer(it) },
                            onOpenAccountSwitcher = { viewModel.openAccountSwitcher() }
                        )
                    }
                    AppScreen.SETTINGS -> {
                        SettingsScreen(
                            userSettings = userSettings,
                            currentInstagramHandle = userProfile.instagramHandle,
                            onSwitchAccount = { viewModel.openAccountSwitcher() },
                            onLogout = { viewModel.logout() },
                            onUpdateSettings = { viewModel.updateSettings(it) },
                            onSetAccent = { viewModel.setAccent(it) },
                            onClearCache = { viewModel.clearCache() },
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                }
            }
        }
    }

    // Modal Sheet 1: Comments
    if (activeCommentsPost != null) {
        val comments = activeCommentsPost?.let { post ->
            listOf(
                com.example.data.model.Comment(
                    id = "c_1",
                    postId = post.id,
                    authorName = "Hasnain Ayaz",
                    authorHandle = "@hasnain_ayaz",
                    authorAvatarRes = com.example.R.drawable.img_creator_avatar,
                    text = "Welcome to Baloora. Designed with modern minimalism and sovereign identity.",
                    timeAgo = "1h ago",
                    likesCount = 14
                ),
                com.example.data.model.Comment(
                    id = "c_2",
                    postId = post.id,
                    authorName = "Maryam Baloch",
                    authorHandle = "@maryam_b",
                    authorAvatarRes = com.example.R.drawable.img_baloch_desert,
                    text = "The typography and mountain contours on this are incredible! 🏔️✨",
                    timeAgo = "30m ago",
                    likesCount = 8
                )
            )
        } ?: emptyList()

        CommentsBottomSheet(
            post = activeCommentsPost,
            comments = comments,
            inputText = commentInputText,
            onInputChange = { viewModel.setCommentInputText(it) },
            onSubmitComment = { viewModel.submitComment() },
            onLikeComment = { viewModel.toggleCommentLike(it) },
            onDismiss = { viewModel.closeComments() }
        )
    }

    // Modal Sheet 2: Share Sheet
    if (activeSharePost != null) {
        ShareModalSheet(
            post = activeSharePost,
            onShareOptionClick = { optionMessage ->
                viewModel.showToast(optionMessage)
                viewModel.closeShare()
            },
            onDismiss = { viewModel.closeShare() }
        )
    }

    // Modal Sheet 3: Post Options Menu
    if (activeOptionsPost != null) {
        val post = activeOptionsPost!!
        PostOptionsMenu(
            post = post,
            onFollowToggle = { viewModel.toggleFollow(post.authorHandle, post.isFollowing) },
            onMuteUser = { viewModel.showToast("Muted posts from ${post.authorHandle}") },
            onHidePost = { viewModel.showToast("Post hidden from feed") },
            onDeletePost = { viewModel.deletePost(post.id) },
            onDismiss = { viewModel.closeOptions() }
        )
    }

    // Modal Sheet 4: Edit Profile Dialog
    if (isEditProfileDialogOpen) {
        EditProfileDialog(
            currentDisplayName = userProfile.displayName,
            currentBio = userProfile.bio,
            currentLocation = userProfile.location,
            onSave = { displayName, bio, location ->
                viewModel.updateProfile(displayName, bio, location)
            },
            onDismiss = { viewModel.closeEditProfileDialog() }
        )
    }

    // Fullscreen Overlay 1: Story Viewer
    activeStoryIndex?.let { storyIndex ->
        StoryViewerScreen(
            stories = stories,
            initialIndex = storyIndex,
            onClose = { viewModel.closeStory() },
            onNextStory = { viewModel.nextStory() },
            onPreviousStory = { viewModel.previousStory() },
            onReact = { emoji -> viewModel.reactToStory(emoji) },
            onReply = { text -> viewModel.replyToStory(text) }
        )
    }

    // Fullscreen Overlay 2: Media Viewer Lightbox
    activeMediaPost?.let { post ->
        MediaViewerScreen(
            post = post,
            onClose = { viewModel.closeMediaViewer() },
            onLikeClick = { viewModel.toggleLike(post) },
            onCommentClick = {
                viewModel.openComments(post)
            },
            onRepostClick = { viewModel.toggleRepost(post) },
            onSaveClick = { viewModel.toggleSave(post) },
            onShareClick = { viewModel.openShare(post) }
        )
    }

    // Modal Sheet 5: Account Switcher
    if (isAccountSwitcherOpen) {
        AccountSwitcherBottomSheet(
            currentProfile = userProfile,
            savedAccounts = savedAccounts,
            onSelectAccount = { username ->
                viewModel.switchAccount(username)
            },
            onAddNewAccount = {
                viewModel.closeAccountSwitcher()
                viewModel.navigateTo(AppScreen.LOGIN)
            },
            onLogout = {
                viewModel.logout()
            },
            onDismiss = {
                viewModel.closeAccountSwitcher()
            }
        )
    }
}
