package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedFilter
import com.example.ui.theme.LocalBalooraColors
import com.example.viewmodel.AppScreen

@Composable
fun BalooraTopBar(
    currentScreen: AppScreen,
    selectedFilter: FeedFilter,
    onFilterSelected: (FeedFilter) -> Unit,
    onDirectMessageClick: () -> Unit,
    onSettingsClick: () -> Unit,
    unreadNotificationsCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Left
                BalooraBrandHeader(isCompact = true)

                // Actions Right
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = customColors.subtleText,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = onDirectMessageClick,
                        modifier = Modifier.testTag("direct_messages_button")
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = customColors.brandGold,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text("2", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Send,
                                contentDescription = "Direct Messages",
                                tint = customColors.brandGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // Feed Filter Selector (Only displayed on Home screen)
            if (currentScreen == AppScreen.HOME) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(customColors.cardBackground)
                        .border(1.dp, customColors.borderStroke, RoundedCornerShape(20.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeedFilter.entries.forEach { filter ->
                        val isSelected = filter == selectedFilter
                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) customColors.brandGold else Color.Transparent,
                            label = "filter_bg"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else customColors.subtleText,
                            label = "filter_text"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(backgroundColor)
                                .clickable { onFilterSelected(filter) }
                                .padding(vertical = 6.dp)
                                .testTag("filter_${filter.name.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter.label,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = textColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class NavItem(
    val screen: AppScreen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : NavItem(AppScreen.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Explore : NavItem(AppScreen.EXPLORE, "Explore", Icons.Filled.Explore, Icons.Outlined.Explore)
    object Create : NavItem(AppScreen.CREATE, "Create", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline)
    object Activity : NavItem(AppScreen.ACTIVITY, "Activity", Icons.Filled.Notifications, Icons.Outlined.Notifications)
    object Profile : NavItem(AppScreen.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun BalooraBottomNav(
    currentScreen: AppScreen,
    onItemSelected: (AppScreen) -> Unit,
    unreadActivityCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current
    val navItems = listOf(
        NavItem.Home,
        NavItem.Explore,
        NavItem.Create,
        NavItem.Activity,
        NavItem.Profile
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = customColors.cardBackground,
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, customColors.borderStroke)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentScreen == item.screen
                val isCreate = item == NavItem.Create

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) customColors.brandGold else customColors.faintText,
                    label = "nav_icon_color"
                )

                if (isCreate) {
                    // Center highlighted Create Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(customColors.brandGold, customColors.brandAmber)
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onItemSelected(item.screen) }
                            .testTag("nav_item_create"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Create Post",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onItemSelected(item.screen) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("nav_item_${item.title.lowercase()}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (item == NavItem.Activity && unreadActivityCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = customColors.brandAmber,
                                        contentColor = Color.Black
                                    ) {
                                        Text("$unreadActivityCount", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = iconColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Subtle glowing dot indicator for active tab
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) customColors.brandGold else Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}
