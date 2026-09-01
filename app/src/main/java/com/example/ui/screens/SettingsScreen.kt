package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FeedDensity
import com.example.data.model.UserSettings
import com.example.ui.components.BalooraGeometricEmblem
import com.example.ui.theme.BalooraAccent
import com.example.ui.theme.ChagaiCrimson
import com.example.ui.theme.GwadarEmerald
import com.example.ui.theme.LocalBalooraColors
import com.example.ui.theme.MakranAzure
import com.example.ui.theme.SulemanSlate
import com.example.ui.theme.ZarwanGold

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.SwitchAccount
import com.example.ui.screens.InstaGradient

@Composable
fun SettingsScreen(
    userSettings: UserSettings,
    currentInstagramHandle: String = "raskolnikov_h1",
    onSwitchAccount: () -> Unit = {},
    onLogout: () -> Unit = {},
    onUpdateSettings: (UserSettings) -> Unit,
    onSetAccent: (BalooraAccent) -> Unit,
    onClearCache: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 8.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = customColors.brandGold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preferences & System",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        // ================= INSTAGRAM ACCOUNT CENTER =================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("instagram_account_center_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = customColors.cardBackground
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1306C).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(InstaGradient)
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF121212)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "IG",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Meta & Instagram Account",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Logged in as @$currentInstagramHandle",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF0095F6),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onSwitchAccount,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0095F6),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwitchAccount,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Switch Account", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onLogout,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Log Out",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ================= CREATOR CREDIT SECTION =================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("creator_credit_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = customColors.cardBackground
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, customColors.brandGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_creator_avatar),
                            contentDescription = "Hasnain Ayaz",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(2.dp, customColors.brandGold, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Crafted by Hasnain Ayaz",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = customColors.brandGold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Instagram: raskolnikov_h1",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Text(
                                text = "Lead Architect & UI Designer",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = customColors.subtleText
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Baloora is built as a next-generation sovereign social platform inspired by Balochistan's rugged mountain terrain, Makran coastal elegance, and Arabian geometric symmetry. For design insights and custom client engineering, connect via Instagram.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = customColors.subtleText,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://instagram.com/raskolnikov_h1")
                                )
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                // Handled gracefully if browser/app isn't present
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("open_creator_instagram_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = customColors.brandGold)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "View Design Insights (@raskolnikov_h1)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // ================= SECTION: APPEARANCE & STYLING =================
        item {
            SectionHeader(icon = Icons.Default.Palette, title = "Appearance & Theming")
        }

        // Dark / Light Mode Switch
        item {
            SettingSwitchRow(
                title = "Dark Theme",
                subtitle = "Enable eye-safe obsidian dark styling",
                checked = userSettings.isDarkMode,
                onCheckedChange = { onUpdateSettings(userSettings.copy(isDarkMode = it)) }
            )
        }

        // True OLED Black Switch
        item {
            SettingSwitchRow(
                title = "True OLED Black",
                subtitle = "Deep pure black background for high contrast & battery saving",
                checked = userSettings.isOledBlack,
                onCheckedChange = { onUpdateSettings(userSettings.copy(isOledBlack = it)) }
            )
        }

        // Accent Color Palette Selector
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Baloora Accent Palette",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Choose a cultural accent inspired by Balochistan geography",
                    style = MaterialTheme.typography.bodySmall.copy(color = customColors.subtleText)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BalooraAccent.entries.forEach { accent ->
                        val isSelected = userSettings.accentName == accent.name
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onSetAccent(accent) }
                                .padding(4.dp)
                                .testTag("accent_${accent.name.lowercase()}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(accent.primary)
                                    .border(
                                        if (isSelected) 3.dp else 1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (accent == BalooraAccent.ZARWAN_GOLD) Color.Black else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = accent.displayName.split(" ").first(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = if (isSelected) customColors.brandGold else customColors.subtleText
                                )
                            )
                        }
                    }
                }
            }
        }

        // Feed Density Selector
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Feed Density",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(customColors.cardBackground)
                        .border(1.dp, customColors.borderStroke, RoundedCornerShape(14.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeedDensity.entries.forEach { density ->
                        val isSelected = userSettings.feedDensity == density
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) customColors.brandGold else Color.Transparent)
                                .clickable { onUpdateSettings(userSettings.copy(feedDensity = density)) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = density.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else customColors.subtleText
                                )
                            )
                        }
                    }
                }
            }
        }

        // Font Scale Slider
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "App Font Scale",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = String.format("%.2fx", userSettings.fontScale),
                        style = MaterialTheme.typography.bodySmall.copy(color = customColors.brandGold)
                    )
                }
                Slider(
                    value = userSettings.fontScale,
                    onValueChange = { onUpdateSettings(userSettings.copy(fontScale = it)) },
                    valueRange = 0.85f..1.25f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = customColors.brandGold,
                        activeTrackColor = customColors.brandGold
                    )
                )
            }
        }

        // ================= SECTION: PRIVACY & SECURITY =================
        item {
            SectionHeader(icon = Icons.Default.Security, title = "Privacy & Safety")
        }

        item {
            SettingSwitchRow(
                title = "Private Account",
                subtitle = "Only approved followers can view your stories and posts",
                checked = userSettings.isPrivateAccount,
                onCheckedChange = { onUpdateSettings(userSettings.copy(isPrivateAccount = it)) }
            )
        }

        item {
            SettingSwitchRow(
                title = "Incognito Story View",
                subtitle = "View friends' stories anonymously without appearing on viewer list",
                checked = userSettings.ghostMode,
                onCheckedChange = { onUpdateSettings(userSettings.copy(ghostMode = it)) }
            )
        }

        item {
            SettingSwitchRow(
                title = "Show Activity Status",
                subtitle = "Allow active contacts to see when you're online in Baloora",
                checked = userSettings.showActivityStatus,
                onCheckedChange = { onUpdateSettings(userSettings.copy(showActivityStatus = it)) }
            )
        }

        // ================= SECTION: NOTIFICATIONS =================
        item {
            SectionHeader(icon = Icons.Default.Notifications, title = "Notification Center")
        }

        item {
            SettingSwitchRow(
                title = "Push Notifications",
                subtitle = "Receive alerts for likes, comments, and direct replies",
                checked = userSettings.notificationsEnabled,
                onCheckedChange = { onUpdateSettings(userSettings.copy(notificationsEnabled = it)) }
            )
        }

        // ================= SECTION: DATA & STORAGE =================
        item {
            SectionHeader(icon = Icons.Default.Storage, title = "Data & Storage")
        }

        item {
            SettingSwitchRow(
                title = "High-Quality Media Uploads",
                subtitle = "Always upload lossless Baloch photography & high bit-rate clips",
                checked = userSettings.highQualityUploads,
                onCheckedChange = { onUpdateSettings(userSettings.copy(highQualityUploads = it)) }
            )
        }

        item {
            SettingSwitchRow(
                title = "Data Saver Mode",
                subtitle = "Reduce mobile data by lowering feed video preview resolution",
                checked = userSettings.dataSaverMode,
                onCheckedChange = { onUpdateSettings(userSettings.copy(dataSaverMode = it)) }
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reclaim Storage Cache",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Clear cached image bitmaps, previews, and temporary files",
                        style = MaterialTheme.typography.bodySmall.copy(color = customColors.subtleText)
                    )
                }

                Button(
                    onClick = onClearCache,
                    colors = ButtonDefaults.buttonColors(containerColor = customColors.elevatedBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, customColors.borderStroke),
                    modifier = Modifier.testTag("clear_cache_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Clear Cache",
                        tint = customColors.brandGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Clear Cache",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // App Footer Info
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BalooraGeometricEmblem(
                    size = 32.dp,
                    animatedSheen = false,
                    primaryColor = customColors.brandGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "BALOORA v2.4.0 (Build 890)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = customColors.subtleText,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "Crafted by Hasnain Ayaz (@raskolnikov_h1)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = customColors.brandGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    val customColors = LocalBalooraColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = customColors.brandGold,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = customColors.brandGold,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val customColors = LocalBalooraColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = customColors.subtleText,
                    lineHeight = 16.sp
                )
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = customColors.brandGold,
                uncheckedThumbColor = customColors.subtleText,
                uncheckedTrackColor = customColors.elevatedBackground
            )
        )
    }
}
