package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MediaType
import com.example.data.model.Post
import com.example.data.model.UserProfile
import com.example.ui.theme.LocalBalooraColors

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Launch
import com.example.ui.screens.InstaGradient

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    userPosts: List<Post>,
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPostClick: (Post) -> Unit,
    onOpenAccountSwitcher: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customColors = LocalBalooraColors.current
    val tabs = listOf(
        Pair("Posts", Icons.Default.GridOn),
        Pair("Shorts", Icons.Default.VideoLibrary),
        Pair("Saved", Icons.Default.Bookmark),
        Pair("Reposts", Icons.Default.Repeat)
    )

    val displayedPosts = when (selectedTab) {
        0 -> userPosts
        1 -> userPosts.filter { it.mediaType == MediaType.SHORT || it.mediaType == MediaType.VIDEO }
        2 -> userPosts.filter { it.isSaved }
        3 -> userPosts.filter { it.isReposted }
        else -> userPosts
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Instagram Handle & Switcher Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenAccountSwitcher() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("profile_account_switcher_trigger")
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(InstaGradient)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = userProfile.instagramHandle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Switch Instagram Account",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://instagram.com/${userProfile.instagramHandle}")
                                )
                                context.startActivity(intent)
                            } catch (_: Exception) { }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Launch,
                            contentDescription = "Open in Instagram",
                            tint = Color(0xFFE1306C),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Profile Header Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Avatar with glowing cultural ring
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(
                                        customColors.brandGold,
                                        customColors.brandAmber,
                                        customColors.brandGold
                                    )
                                )
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(customColors.cardBackground)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_creator_avatar),
                            contentDescription = userProfile.displayName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Stats row
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(count = "${userProfile.postsCount}", label = "Posts")
                        ProfileStatItem(count = "28.4K", label = "Followers")
                        ProfileStatItem(count = "${userProfile.followingCount}", label = "Following")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Display Name & Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userProfile.displayName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    if (userProfile.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = customColors.brandGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "@${userProfile.username}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.brandGold,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                // Cultural badge pill
                Box(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(customColors.brandGold.copy(alpha = 0.15f))
                        .border(1.dp, customColors.brandGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "⚡ ${userProfile.culturalTitle} • ${userProfile.location}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = customColors.brandGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }

                // Bio
                Text(
                    text = userProfile.bio,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons (Edit Profile, Settings)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onEditProfileClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("edit_profile_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = customColors.brandGold)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Edit Profile",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("profile_settings_shortcut"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, customColors.borderStroke)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = customColors.subtleText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Tab Selector Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = customColors.brandGold,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = customColors.brandGold,
                        height = 2.5.dp
                    )
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                tabs.forEachIndexed { index, pair ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { onSelectTab(index) },
                        icon = {
                            Icon(
                                imageVector = pair.second,
                                contentDescription = pair.first,
                                tint = if (selectedTab == index) customColors.brandGold else customColors.subtleText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }
        }

        // Grid Content
        if (displayedPosts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📁", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No media in ${tabs[selectedTab].first.lowercase()}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = customColors.subtleText)
                    )
                }
            }
        } else {
            val chunkedPosts = displayedPosts.chunked(3)
            items(chunkedPosts) { rowPosts: List<Post> ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 1.5.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (post in rowPosts) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .background(Color.Black)
                                .clickable { onPostClick(post) }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = post.mediaResId ?: R.drawable.img_baloch_mountains
                                ),
                                contentDescription = post.caption,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            if (post.mediaType == MediaType.SHORT || post.mediaType == MediaType.VIDEO) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Video",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(16.dp)
                                )
                            }
                        }
                    }
                    if (rowPosts.size < 3) {
                        val remaining = 3 - rowPosts.size
                        for (i in 0 until remaining) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(count: String, label: String) {
    val customColors = LocalBalooraColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = customColors.subtleText,
                fontSize = 11.sp
            )
        )
    }
}
