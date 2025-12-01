package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductBanner(
    products: List<Product>
) {
    val infinitelyScrollableCount = Int.MAX_VALUE
    val startIndex = Int.MAX_VALUE / 2
    val bannerState = rememberLazyListState(startIndex)
    val scope = rememberCoroutineScope()
    val currentIndex by remember {
        derivedStateOf { bannerState.firstVisibleItemIndex }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            bannerState.animateScrollToItem(currentIndex + 1)
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            state = bannerState,
            modifier = Modifier.fillMaxWidth().height(200.dp),
        ) {
            items(count = infinitelyScrollableCount) { index ->
                val realIndex = index % products.size
                val product = products[realIndex]
                Card(
                    modifier = Modifier.width(350.dp).height(200.dp).padding(4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    CustomAsyncImage(
                        url = "${BASE_URL}${product.imageUrl}",
                        contentDescription = product.description
                    )
                }
            }
        }
        IconButton(
            onClick = {
                scope.launch {
                    bannerState.animateScrollToItem(index = currentIndex - 1)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
                .pointerHoverIcon(icon = PointerIcon.Hand),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                contentDescription = "Previous Product Card",
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(
            onClick = {
                scope.launch {
                    bannerState.animateScrollToItem(index = currentIndex + 1)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 20.dp)
                .pointerHoverIcon(icon = PointerIcon.Hand),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "Next Product Card",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}