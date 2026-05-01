package com.m1x.gymmer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun hexagonPath(cx: Float, cy: Float, R: Float, startAngleDeg: Float): Path {
    val path = Path()
    for (i in 0 until 6) {
        val angle = ((PI / 180.0) * (60.0 * i + startAngleDeg)).toFloat()
        val x = cx + R * cos(angle)
        val y = cy + R * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

/**
 * Hexagonal badge mark with a lightning bolt — drawn entirely in Compose Canvas
 * so it scales perfectly at any density and size.
 */
@Composable
fun GymmerLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val R = w * 0.44f

        // ── Outer hexagon ─────────────────────────────────────────────────
        val hexPath = hexagonPath(cx, cy, R, startAngleDeg = -90f)

        // Dark fill
        drawPath(hexPath, Color(0xFF0D0D0D))

        // Wide outer glow ring
        drawPath(
            hexPath,
            LimeGreen.copy(alpha = 0.07f),
            style = Stroke(width = w * 0.10f, join = StrokeJoin.Round)
        )

        // Medium glow ring
        drawPath(
            hexPath,
            LimeGreen.copy(alpha = 0.12f),
            style = Stroke(width = w * 0.05f, join = StrokeJoin.Miter)
        )

        // Crisp primary stroke
        drawPath(
            hexPath,
            LimeGreen,
            style = Stroke(width = w * 0.026f, join = StrokeJoin.Miter)
        )

        // ── Inner decorative hexagon (flat-top, rotated 30°) ─────────────
        val innerHexPath = hexagonPath(cx, cy, R * 0.54f, startAngleDeg = -60f)
        drawPath(
            innerHexPath,
            LimeGreen.copy(alpha = 0.20f),
            style = Stroke(width = w * 0.009f)
        )

        // ── Lightning bolt ────────────────────────────────────────────────
        val boltPath = Path().apply {
            moveTo(cx + w * 0.090f,  cy - h * 0.370f)   // top apex (A)
            lineTo(cx - w * 0.185f,  cy + h * 0.040f)   // mid-left  (B)
            lineTo(cx - w * 0.030f,  cy + h * 0.040f)   // inner notch low-right (C)
            lineTo(cx - w * 0.115f,  cy + h * 0.370f)   // bottom apex (D)
            lineTo(cx + w * 0.165f,  cy - h * 0.040f)   // mid-right (E)
            lineTo(cx + w * 0.010f,  cy - h * 0.040f)   // inner notch high-left (F)
            close()
        }

        // Soft halo around bolt
        drawPath(
            boltPath,
            LimeGreen.copy(alpha = 0.25f),
            style = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Tighter inner glow
        drawPath(
            boltPath,
            LimeGreen.copy(alpha = 0.40f),
            style = Stroke(width = w * 0.035f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Solid bolt fill (on top of everything)
        drawPath(boltPath, LimeGreen)

        // ── Corner accent dots ────────────────────────────────────────────
        val dotR = w * 0.028f
        for (i in 0 until 6) {
            val angle = ((PI / 180.0) * (60.0 * i - 90.0)).toFloat()
            val vx = cx + R * cos(angle)
            val vy = cy + R * sin(angle)
            drawCircle(LimeGreen.copy(alpha = 0.25f), dotR * 3.0f, Offset(vx, vy))
            drawCircle(LimeGreen.copy(alpha = 0.60f), dotR * 1.6f, Offset(vx, vy))
            drawCircle(LimeGreen, dotR, Offset(vx, vy))
        }
    }
}

/**
 * Full logo: mark + wordmark + tagline.
 */
@Composable
fun GymmerLogoFull(
    modifier: Modifier = Modifier,
    logoSize: Dp = 140.dp,
    showTagline: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GymmerLogoMark(size = logoSize)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "GYMOPS",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                ),
                color = Color.White
            )
            if (showTagline) {
                Text(
                    text = "KINETIC VOLT",
                    style = MaterialTheme.typography.labelLarge,
                    color = LimeGreen,
                    letterSpacing = 8.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GymmerLogoMarkPreview() {
    GymmerTheme {
        Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
            GymmerLogoMark(size = 200.dp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GymmerLogoFullPreview() {
    GymmerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            GymmerLogoFull()
        }
    }
}
