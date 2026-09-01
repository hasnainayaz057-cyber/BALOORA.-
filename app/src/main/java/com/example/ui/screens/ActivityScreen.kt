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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActivityNotification
import com.example.data.model.ActivityType
import com.example.ui.theme.ChagaiCrimson
import com.example.ui.theme.GwadarEmerald
import com.example.ui.theme.LocalBalooraColors
import com.example.ui.theme.MakranAzure

@Composable
fun ActivityScreen(
    notifications: List<ActivityNotification>,
    selectedFilterTab: Int,
    onSelectFilterTab: (Int) -> Unit,
    onNotificationClick: (ActivityNotification) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onFollowBack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current
    val filterTabs = listOf("All", "Likes", "Comments", "Follows", "Mentions")

    val filteredNotifications = when (selectedFilterTab) {
        1 -> notifications.filter { it.type == ActivityType.LIKE }
        2 -> notifications.filter { it.type == ActivityType.COMMENT || it.type == ActivityType.STORY_REPLY }
        3 -> notifications.filter { it.type == ActivityType.FOLLOW }
        4 -> notifications.filter { it.type == ActivityType.MENTION || it.type == ActivityType.REPOST }
        else -> notifications
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("activity_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header with Mark all read
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activity Center",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                TextButton(onClick = onMarkAllAsRead) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Mark all read",
                        tint = customColors.brandGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mark all read",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = customColors.brandGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Filter Pills
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterTabs.indices.toList()) { index ->
                    val isSelected = index == selectedFilterTab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) customColors.brandGold else customColors.cardBackground
                            )
                            .border(
                                1.dp,
                                if (isSelected) customColors.brandGold else customColors.borderStroke,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectFilterTab(index) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("activity_filter_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filterTabs[index],
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else customColors.subtleText
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Empty state
        if (filteredNotifications.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🔔", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No notifications yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Interactions from your followers and friends will appear here in real-time.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = customColors.subtleText,
                            lineHeight = 20.sp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Notification Items
        items(filteredNotifications, key = { it.id }) { notif ->
            val iconAndColor: Pair<ImageVector, Color> = when (notif.type) {
                ActivityType.LIKE -> Pair(Icons.Default.Favorite, ChagaiCrimson)
                ActivityType.COMMENT, ActivityType.STORY_REPLY -> Pair(Icons.Default.ChatBubble, MakranAzure)
                ActivityType.FOLLOW -> Pair(Icons.Default.PersonAdd, customColors.brandGold)
                ActivityType.MENTION -> Pair(Icons.Default.AlternateEmail, GwadarEmerald)
                ActivityType.REPOST -> Pair(Icons.Default.Repeat, customColors.brandGold)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNotificationClick(notif) }
                    .testTag("notification_item_${notif.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (notif.isRead) customColors.cardBackground else customColors.elevatedBackground
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (notif.isRead) customColors.borderStroke else customColors.brandGold.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar with type badge
                    Box(
                        modifier = Modifier.size(46.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                id = notif.actorAvatarRes ?: R.drawable.img_creator_avatar
                            ),
                            contentDescription = notif.actorName,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(iconAndColor.second),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconAndColor.first,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Text Details
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = notif.actorName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = notif.timeAgo,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    color = customColors.subtleText
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notif.text,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Action or Media Preview
                    if (notif.type == ActivityType.FOLLOW) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onFollowBack(notif.actorHandle) },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = customColors.brandGold),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "Follow Back",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    } else if (notif.targetMediaResId != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = notif.targetMediaResId),
                            contentDescription = "Target Media",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
