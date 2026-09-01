package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalBalooraColors
import com.example.ui.theme.ZarwanGold
import com.example.ui.theme.ZarwanGoldLight

/**
 * Geometric emblem inspired by Baloch diamond patterns and mountain silhouettes,
 * synthesized with Arabian geometric symmetry.
 */
@Composable
fun BalooraGeometricEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    animatedSheen: Boolean = true,
    primaryColor: Color = ZarwanGold,
    accentColor: Color = ZarwanGoldLight
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_sheen")
    val sheenProgress by infiniteTransition.animateFloat(
        initialValue = -1.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sheen_pos"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f

        // Outer Diamond
        val outerPath = Path().apply {
            moveTo(cx, 4f)
            lineTo(w - 4f, cy)
            lineTo(cx, h - 4f)
            lineTo(4f, cy)
            close()
        }

        // Inner Mountain Contours (Baloch mountain geometry)
        val mountainPath = Path().apply {
            moveTo(cx * 0.4f, cy * 1.35f)
            lineTo(cx * 0.75f, cy * 0.75f)
            lineTo(cx, cy * 1.05f)
            lineTo(cx * 1.25f, cy * 0.65f)
            lineTo(cx * 1.6f, cy * 1.35f)
        }

        // Central Star Apex
        val apexPath = Path().apply {
            moveTo(cx, cy * 0.35f)
            lineTo(cx + 6f, cy * 0.55f)
            lineTo(cx, cy * 0.75f)
            lineTo(cx - 6f, cy * 0.55f)
            close()
        }

        val strokeBrush = if (animatedSheen) {
            val sheenCenter = w * sheenProgress
            Brush.linearGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.5f),
                    primaryColor,
                    Color.White.copy(alpha = 0.95f),
                    primaryColor,
                    primaryColor.copy(alpha = 0.5f)
                ),
                start = Offset(sheenCenter - w * 0.4f, sheenCenter - h * 0.4f),
                end = Offset(sheenCenter + w * 0.4f, sheenCenter + h * 0.4f)
            )
        } else {
            Brush.linearGradient(
                colors = listOf(primaryColor, accentColor),
                start = Offset.Zero,
                end = Offset(w, h)
            )
        }

        // Draw ambient glow
        drawPath(
            path = outerPath,
            brush = Brush.radialGradient(
                colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                center = Offset(cx, cy),
                radius = w * 0.6f
            )
        )

        // Draw strokes
        drawPath(
            path = outerPath,
            brush = strokeBrush,
            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        drawPath(
            path = mountainPath,
            brush = strokeBrush,
            style = Stroke(width = 2.0.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        drawPath(
            path = apexPath,
            brush = strokeBrush,
            style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * Full Brand Header / Title with Logo & 'Powered by البلوشی'
 */
@Composable
fun BalooraBrandHeader(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    val customColors = LocalBalooraColors.current

    if (isCompact) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BalooraGeometricEmblem(
                size = 28.dp,
                animatedSheen = true,
                primaryColor = customColors.brandGold,
                accentColor = customColors.brandAmber
            )
            Column {
                Text(
                    text = "BALOORA",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.5.sp,
                        color = customColors.brandGold
                    )
                )
                Text(
                    text = "البلوشی",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                        color = customColors.subtleText
                    )
                )
            }
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BalooraGeometricEmblem(
                size = 64.dp,
                animatedSheen = true,
                primaryColor = customColors.brandGold,
                accentColor = customColors.brandAmber
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "BALOORA",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 5.sp,
                    color = customColors.brandGold
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Powered by البلوشی",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp,
                    color = customColors.subtleText
                )
            )
        }
    }
}
