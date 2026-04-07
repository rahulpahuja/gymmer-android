package com.m1x.gymmer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        delay(100) // Small delay to ensure initial state is rendered
        startAnimation = true
        delay(2500) // Increased slightly to allow full animation to finish
        onTimeout()
    }

    SplashContent(
        alpha = alphaAnim.value,
        scale = scaleAnim.value
    )
}

@Composable
fun SplashContent(
    alpha: Float,
    scale: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            Text(
                text = "GYMOPS",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                ),
                color = Color.White
            )
            Text(
                text = "KINETIC VOLT",
                style = MaterialTheme.typography.labelLarge,
                color = LimeGreen,
                letterSpacing = 8.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GymmerTheme {
        SplashContent(alpha = 1f, scale = 1f)
    }
}
