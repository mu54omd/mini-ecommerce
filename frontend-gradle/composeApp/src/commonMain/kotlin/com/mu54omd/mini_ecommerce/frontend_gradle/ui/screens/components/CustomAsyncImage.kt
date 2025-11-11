package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CustomAsyncImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    errorTint: Color = Color.Red,
    size: Dp = 100.dp
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    )
    val state by painter.state.collectAsState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop
                )
            }

            is AsyncImagePainter.State.Error -> {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = contentDescription,
                    tint = errorTint,
                    modifier = Modifier.size(size)
                )
            }

            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }

            else -> {

            }
        }
    }
}