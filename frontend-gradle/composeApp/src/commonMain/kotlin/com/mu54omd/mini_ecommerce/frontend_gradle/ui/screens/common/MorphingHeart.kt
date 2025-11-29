package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MorphingHeart() {
    var isFavorite by remember { mutableStateOf(false) }

    val progress by animateFloatAsState(
        targetValue = if (isFavorite) 1f else 0f,
        animationSpec = tween(durationMillis = 600)
    )

    val heartEmpty = listOf(
        Offset(50f, 15f),
        Offset(90f, 35f),
        Offset(80f, 70f),
        Offset(50f, 90f),
        Offset(20f, 70f),
        Offset(10f, 35f)
    )

    val heartFull = listOf(
        Offset(50f, 0f),
        Offset(100f, 40f),
        Offset(85f, 80f),
        Offset(50f, 100f),
        Offset(15f, 80f),
        Offset(0f, 40f)
    )

    fun lerpPath(p1: List<Offset>, p2: List<Offset>, t: Float): Path {
        val path = Path()
        p1.zip(p2).forEachIndexed { index, (start, end) ->
            val x = start.x + (end.x - start.x) * t
            val y = start.y + (end.y - start.y) * t
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return path
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val path = lerpPath(heartEmpty, heartFull, progress)
            drawPath(path, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isFavorite = !isFavorite }) {
            Text("Toggle Heart")
        }
    }
}

@Preview
@Composable
fun MorphingHeartPreview(){
    MiniECommerceTheme {
        Surface {
            MorphingHeart()
        }
    }
}


