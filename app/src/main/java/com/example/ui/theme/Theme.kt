package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BalooraCustomColors(
    val brandGold: Color = ZarwanGold,
    val brandAmber: Color = ZarwanAmber,
    val cardBackground: Color = DarkSurfaceCard,
    val elevatedBackground: Color = DarkSurfaceElevated,
    val borderStroke: Color = DarkSurfaceBorder,
    val subtleText: Color = DarkTextSecondary,
    val faintText: Color = DarkTextTertiary,
    val cultureAccent: Color = MakranAzure,
    val isDark: Boolean = true
)

val LocalBalooraColors = staticCompositionLocalOf { BalooraCustomColors() }

@Composable
fun BalooraTheme(
    darkTheme: Boolean = true,
    isOledBlack: Boolean = false,
    accent: BalooraAccent = BalooraAccent.ZARWAN_GOLD,
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val primaryColor = accent.primary
    val secondaryColor = accent.secondary

    val darkScheme = darkColorScheme(
        primary = primaryColor,
        onPrimary = ObsidianBlack,
        primaryContainer = primaryColor.copy(alpha = 0.2f),
        onPrimaryContainer = ZarwanGoldLight,
        secondary = secondaryColor,
        onSecondary = ObsidianBlack,
        secondaryContainer = secondaryColor.copy(alpha = 0.2f),
        onSecondaryContainer = Color.White,
        tertiary = MakranAzure,
        background = if (isOledBlack) Color.Black else ObsidianBlack,
        onBackground = DarkTextPrimary,
        surface = if (isOledBlack) Color(0xFF070709) else DarkSurface,
        onSurface = DarkTextPrimary,
        surfaceVariant = DarkSurfaceElevated,
        onSurfaceVariant = DarkTextSecondary,
        outline = DarkSurfaceBorder,
        outlineVariant = DarkSurfaceBorder.copy(alpha = 0.5f)
    )

    val lightScheme = lightColorScheme(
        primary = primaryColor,
        onPrimary = Color.White,
        primaryContainer = primaryColor.copy(alpha = 0.15f),
        onPrimaryContainer = ZarwanGoldDark,
        secondary = secondaryColor,
        onSecondary = Color.White,
        secondaryContainer = secondaryColor.copy(alpha = 0.15f),
        onSecondaryContainer = LightTextPrimary,
        tertiary = MakranAzure,
        background = IvoryWhite,
        onBackground = LightTextPrimary,
        surface = LightSurface,
        onSurface = LightTextPrimary,
        surfaceVariant = LightSurfaceCard,
        onSurfaceVariant = LightTextSecondary,
        outline = LightSurfaceBorder,
        outlineVariant = LightSurfaceBorder.copy(alpha = 0.6f)
    )

    val customColors = if (darkTheme) {
        BalooraCustomColors(
            brandGold = primaryColor,
            brandAmber = secondaryColor,
            cardBackground = if (isOledBlack) Color(0xFF08080C) else DarkSurfaceCard,
            elevatedBackground = DarkSurfaceElevated,
            borderStroke = DarkSurfaceBorder,
            subtleText = DarkTextSecondary,
            faintText = DarkTextTertiary,
            cultureAccent = accent.secondary,
            isDark = true
        )
    } else {
        BalooraCustomColors(
            brandGold = primaryColor,
            brandAmber = secondaryColor,
            cardBackground = LightSurfaceCard,
            elevatedBackground = LightSurface,
            borderStroke = LightSurfaceBorder,
            subtleText = LightTextSecondary,
            faintText = LightTextTertiary,
            cultureAccent = accent.secondary,
            isDark = false
        )
    }

    val colorScheme = if (darkTheme) darkScheme else lightScheme

    CompositionLocalProvider(LocalBalooraColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
