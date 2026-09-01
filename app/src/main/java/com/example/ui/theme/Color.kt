package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Brand Palette - Baloch Gold & Obsidian Luxury
val ZarwanGold = Color(0xFFD4AF37)
val ZarwanGoldLight = Color(0xFFF3E5AB)
val ZarwanGoldDark = Color(0xFFAA820A)
val ZarwanAmber = Color(0xFFE5A93C)

// Cultural Accents
val MakranAzure = Color(0xFF00B4D8)
val MakranTeal = Color(0xFF0077B6)
val ChagaiCrimson = Color(0xFFD62828)
val SulemanSlate = Color(0xFF8E9AAF)
val DesertSand = Color(0xFFE9DAC1)
val GwadarEmerald = Color(0xFF2A9D8F)

// Dark Theme Surfaces
val ObsidianBlack = Color(0xFF08070B)
val DarkSurface = Color(0xFF110F17)
val DarkSurfaceElevated = Color(0xFF1A1724)
val DarkSurfaceCard = Color(0xFF15131E)
val DarkSurfaceBorder = Color(0xFF2B2638)
val DarkTextPrimary = Color(0xFFF5F4F8)
val DarkTextSecondary = Color(0xFFA8A3B8)
val DarkTextTertiary = Color(0xFF6B667C)

// Light Theme Surfaces
val IvoryWhite = Color(0xFFFDFBF7)
val LightSurface = Color(0xFFF5F1EA)
val LightSurfaceCard = Color(0xFFFFFFFF)
val LightSurfaceBorder = Color(0xFFE6DFD3)
val LightTextPrimary = Color(0xFF17141D)
val LightTextSecondary = Color(0xFF5E5868)
val LightTextTertiary = Color(0xFF918A9C)

// Accent Palette Presets
enum class BalooraAccent(val displayName: String, val primary: Color, val secondary: Color) {
    ZARWAN_GOLD("Zarwan Gold", ZarwanGold, ZarwanAmber),
    MAKRAN_AZURE("Makran Azure", MakranAzure, MakranTeal),
    CHAGAI_CRIMSON("Chagai Crimson", ChagaiCrimson, ZarwanAmber),
    SULEMAN_SLATE("Suleman Slate", SulemanSlate, ZarwanGoldLight),
    GWADAR_EMERALD("Gwadar Emerald", GwadarEmerald, MakranAzure);

    companion object {
        fun fromName(name: String): BalooraAccent {
            return try {
                valueOf(name)
            } catch (_: Exception) {
                ZARWAN_GOLD
            }
        }
    }
}
