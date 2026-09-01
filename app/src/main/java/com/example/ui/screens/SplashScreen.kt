package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BalooraGeometricEmblem
import com.example.ui.theme.LocalBalooraColors
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ZarwanAmber
import com.example.ui.theme.ZarwanGold
import com.example.ui.theme.ZarwanGoldLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalBalooraColors.current
    var startAnimation by remember { mutableStateOf(false) }

    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.75f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "logo_scale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "logo_alpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 350),
        label = "text_alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F0D15),
                        ObsidianBlack,
                        Color(0xFF16120E)
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Subtle background landscape silhouette
        Image(
            painter = painterResource(id = R.drawable.img_baloch_mountains),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.08f),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Animated Baloora Emblem
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                BalooraGeometricEmblem(
                    size = 100.dp,
                    animatedSheen = true,
                    primaryColor = ZarwanGold,
                    accentColor = ZarwanGoldLight
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // BALOORA Title
            Text(
                text = "BALOORA",
                modifier = Modifier.alpha(textAlpha),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp,
                    color = ZarwanGold,
                    fontSize = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Powered by البلوشی
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Text(
                    text = "Powered by",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = customColors.subtleText,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "البلوشی",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = ZarwanGoldLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 24.sp
                    )
                )
            }
        }
    }
}
