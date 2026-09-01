package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.ui.theme.ChagaiCrimson
import com.example.ui.theme.LocalBalooraColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    post: Post?,
    comments: List<Comment>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSubmitComment: () -> Unit,
    onLikeComment: (Comment) -> Unit,
    onDismiss: () -> Unit
) {
    if (post == null) return
    val customColors = LocalBalooraColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = customColors.cardBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(customColors.subtleText.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(horizontal = 16.dp)
        ) {
            // Sheet Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Comments (${post.commentsCount + comments.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = customColors.subtleText
                    )
                }
            }

            HorizontalDivider(color = customColors.borderStroke, modifier = Modifier.padding(vertical = 8.dp))

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (comments.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💬",
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Be the first to share your thoughts",
                                style = MaterialTheme.typography.bodyMedium.copy(color = customColors.subtleText)
                            )
                        }
                    }
                } else {
                    items(comments, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.Top
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = comment.authorAvatarRes ?: R.drawable.img_creator_avatar
                                    ),
                                    contentDescription = comment.authorName,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = comment.authorName,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = comment.timeAgo,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = customColors.subtleText
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = comment.text,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onLikeComment(comment) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like Comment",
                                    tint = if (comment.isLiked) ChagaiCrimson else customColors.subtleText,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = customColors.borderStroke, modifier = Modifier.padding(vertical = 6.dp))

            // Comment Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_text_field"),
                    placeholder = {
                        Text("Add a comment...", color = customColors.subtleText)
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSubmitComment,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(customColors.brandGold)
                        .testTag("submit_comment_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Post Comment",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareModalSheet(
    post: Post?,
    onShareOptionClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (post == null) return
    val customColors = LocalBalooraColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = customColors.cardBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Share Post",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ShareActionItem(icon = Icons.Default.ContentCopy, title = "Copy Link") {
                    onShareOptionClick("Link copied to clipboard")
                }
                ShareActionItem(icon = Icons.Default.Share, title = "External App") {
                    onShareOptionClick("Shared to system sheet")
                }
                ShareActionItem(icon = Icons.Default.QrCode, title = "QR Code") {
                    onShareOptionClick("Baloora QR Code generated")
                }
                ShareActionItem(icon = Icons.Default.Send, title = "Direct Message") {
                    onShareOptionClick("Sent via Baloora Direct")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ShareActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val customColors = LocalBalooraColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(customColors.elevatedBackground)
                .border(1.dp, customColors.borderStroke, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = customColors.brandGold,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = customColors.subtleText
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostOptionsMenu(
    post: Post?,
    onFollowToggle: () -> Unit,
    onMuteUser: () -> Unit,
    onHidePost: () -> Unit,
    onDeletePost: () -> Unit,
    onDismiss: () -> Unit
) {
    if (post == null) return
    val customColors = LocalBalooraColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = customColors.cardBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OptionRow(
                icon = if (post.isFollowing) Icons.Default.Block else Icons.Default.Send,
                title = if (post.isFollowing) "Unfollow ${post.authorHandle}" else "Follow ${post.authorHandle}",
                color = MaterialTheme.colorScheme.onBackground
            ) {
                onFollowToggle()
                onDismiss()
            }

            OptionRow(
                icon = Icons.Default.VolumeMute,
                title = "Mute @${post.authorHandle}",
                color = MaterialTheme.colorScheme.onBackground
            ) {
                onMuteUser()
                onDismiss()
            }

            OptionRow(
                icon = Icons.Default.VisibilityOff,
                title = "Hide this post",
                color = MaterialTheme.colorScheme.onBackground
            ) {
                onHidePost()
                onDismiss()
            }

            OptionRow(
                icon = Icons.Default.Flag,
                title = "Report post",
                color = ChagaiCrimson
            ) {
                onDismiss()
            }

            if (post.authorHandle == "@hasnain_ayaz") {
                OptionRow(
                    icon = Icons.Default.Delete,
                    title = "Delete Post",
                    color = ChagaiCrimson
                ) {
                    onDeletePost()
                    onDismiss()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    title: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = color,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun EditProfileDialog(
    currentDisplayName: String,
    currentBio: String,
    currentLocation: String,
    onSave: (displayName: String, bio: String, location: String) -> Unit,
    onDismiss: () -> Unit
) {
    val customColors = LocalBalooraColors.current
    var name by remember { mutableStateOf(currentDisplayName) }
    var bio by remember { mutableStateOf(currentBio) }
    var location by remember { mutableStateOf(currentLocation) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = customColors.cardBackground,
        title = {
            Text(
                text = "Edit Baloora Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke
                    )
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke
                    )
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.brandGold,
                        unfocusedBorderColor = customColors.borderStroke
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, bio, location) },
                colors = ButtonDefaults.buttonColors(containerColor = customColors.brandGold)
            ) {
                Text("Save", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = customColors.subtleText)
            }
        }
    )
}
