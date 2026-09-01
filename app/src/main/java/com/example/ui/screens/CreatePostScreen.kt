package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FeedDensity
import com.example.data.model.MediaType
import com.example.data.model.Post
import com.example.data.model.UserDraft
import com.example.ui.components.PostCard
import com.example.ui.theme.LocalBalooraColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    mediaType: MediaType,
    onMediaTypeChange: (MediaType) -> Unit,
    selectedMediaRes: Int,
    onSelectMediaRes: (Int) -> Unit,
    caption: String,
    onCaptionChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    hashtags: String,
    onHashtagsChange: (String) -> Unit,
    mentions: String,
    onMentionsChange: (String) -> Unit,
    isPreviewMode: Boolean,
    onTogglePreview: () -> Unit,
    onPublish: () -> Unit,
    onSaveDraft: () -> Unit,
    drafts: List<UserDraft>,
    onLoadDraft: (UserDraft) -> Unit,
    onDeleteDraft: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current
    var showDraftsList by remember { mutableStateOf(false) }

    val presetImages = listOf(
        Pair(R.drawable.img_baloch_mountains, "Hingol Mountains"),
        Pair(R.drawable.img_baloch_desert, "Gwadar Dunes"),
        Pair(R.drawable.img_baloora_icon, "Baloora Emblem"),
        Pair(R.drawable.img_creator_avatar, "Creator Profile")
    )

    val hashtagSuggestions = listOf(
        "#Baloora", "#Balochistan", "#Heritage", "#Zarwan", "#Makran", "#ModernCulture", "#Photography"
    )

    val locationSuggestions = listOf(
        "Hingol National Park, Balochistan",
        "Gwadar Hammerhead Coastline",
        "Quetta Valley Mountains",
        "Chagai Desert Dunes",
        "Makran Coastal Highway"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("create_post_screen"),
        contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector Tab (Photo, Reel / Short, Video, Story)
        item {
            TabRow(
                selectedTabIndex = mediaType.ordinal,
                containerColor = customColors.cardBackground,
                contentColor = customColors.brandGold,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[mediaType.ordinal]),
                        color = customColors.brandGold,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, customColors.borderStroke, RoundedCornerShape(14.dp))
            ) {
                MediaType.entries.forEach { type ->
                    Tab(
                        selected = mediaType == type,
                        onClick = { onMediaTypeChange(type) },
                        text = {
                            Text(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontWeight = if (mediaType == type) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }
        }

        // Live Preview Toggle & Drafts Management
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onTogglePreview,
                    modifier = Modifier.testTag("toggle_preview_button"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, customColors.brandGold)
                ) {
                    Icon(
                        imageVector = Icons.Default.Preview,
                        contentDescription = "Preview",
                        tint = customColors.brandGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPreviewMode) "Exit Preview" else "Live Preview",
                        color = customColors.brandGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row {
                    OutlinedButton(
                        onClick = onSaveDraft,
                        modifier = Modifier.testTag("save_draft_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Drafts",
                            tint = customColors.subtleText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save Draft", color = customColors.subtleText, fontSize = 12.sp)
                    }

                    if (drafts.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        OutlinedButton(
                            onClick = { showDraftsList = !showDraftsList }
                        ) {
                            Text("Drafts (${drafts.size})", color = customColors.brandGold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Saved Drafts Dropdown View
        if (showDraftsList && drafts.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = customColors.elevatedBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, customColors.borderStroke)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Saved Drafts",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = customColors.brandGold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        drafts.forEach { draft ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLoadDraft(draft)
                                        showDraftsList = false
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = draft.caption.ifBlank { "Untitled ${draft.mediaType.name} draft" },
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = draft.location.ifBlank { "No location set" },
                                        style = MaterialTheme.typography.labelSmall.copy(color = customColors.subtleText)
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteDraft(draft.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Draft",
                                        tint = customColors.subtleText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Preview Card
        if (isPreviewMode) {
            item {
                Text(
                    text = "Live Feed Preview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = customColors.brandGold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                PostCard(
                    post = Post(
                        id = "preview",
                        authorName = "Hasnain Ayaz",
                        authorHandle = "@hasnain_ayaz",
                        authorAvatarRes = R.drawable.img_creator_avatar,
                        isVerified = true,
                        location = location,
                        timeAgo = "Just now",
                        caption = caption.ifBlank { "Sample caption preview with Balochistan landscape photography." },
                        hashtags = hashtags.split(" ", ",").filter { it.isNotBlank() },
                        mediaResId = selectedMediaRes,
                        mediaType = mediaType,
                        likesCount = 0,
                        commentsCount = 0
                    ),
                    feedDensity = FeedDensity.COZY,
                    onLikeClick = {},
                    onCommentClick = {},
                    onShareClick = {},
                    onRepostClick = {},
                    onSaveClick = {},
                    onOptionsClick = {},
                    onMediaClick = {},
                    onAuthorClick = {}
                )
            }
        }

        // Media Selector Presets
        item {
            Text(
                text = "Select Asset / Media",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(presetImages) { (resId, label) ->
                    val isSelected = resId == selectedMediaRes
                    Card(
                        modifier = Modifier
                            .size(width = 110.dp, height = 90.dp)
                            .clickable { onSelectMediaRes(resId) },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) customColors.brandGold else customColors.borderStroke
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = label,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(customColors.brandGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.65f))
                                    .padding(vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        color = Color.White
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Caption Input
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Caption & Story",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${caption.length}/500",
                        style = MaterialTheme.typography.bodySmall.copy(color = customColors.subtleText)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = caption,
                    onValueChange = { if (it.length <= 500) onCaptionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("create_caption_input"),
                    placeholder = {
                        Text(
                            "Share your story, thoughts, or architectural notes on Balochistan...",
                            color = customColors.subtleText
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke,
                        focusedContainerColor = customColors.cardBackground,
                        unfocusedContainerColor = customColors.cardBackground
                    )
                )
            }
        }

        // Hashtags Suggestions
        item {
            Column {
                Text(
                    text = "Hashtags",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    hashtagSuggestions.forEach { tag ->
                        val isAdded = hashtags.contains(tag)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isAdded) customColors.brandGold else customColors.cardBackground)
                                .border(
                                    1.dp,
                                    if (isAdded) customColors.brandGold else customColors.borderStroke,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    if (isAdded) {
                                        onHashtagsChange(hashtags.replace(tag, "").trim())
                                    } else {
                                        onHashtagsChange("$hashtags $tag".trim())
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (isAdded) MaterialTheme.colorScheme.onPrimary else customColors.brandGold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Location Tagging
        item {
            Column {
                Text(
                    text = "Location Tag",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = onLocationChange,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location",
                            tint = customColors.brandGold
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke,
                        focusedContainerColor = customColors.cardBackground,
                        unfocusedContainerColor = customColors.cardBackground
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(locationSuggestions) { loc ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(customColors.elevatedBackground)
                                .border(1.dp, customColors.borderStroke, RoundedCornerShape(12.dp))
                                .clickable { onLocationChange(loc) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = loc,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = customColors.subtleText
                                )
                            )
                        }
                    }
                }
            }
        }

        // Publish Button
        item {
            Button(
                onClick = onPublish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("publish_post_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customColors.brandGold)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Publish to Baloora",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}
