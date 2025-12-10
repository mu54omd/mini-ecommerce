package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ProductBanner(
    bannerTitle: String,
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    val backBrush = Brush.horizontalGradient(
        listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.primaryContainer,
            Color.Transparent
        )
    )
    val frontBrush = Brush.radialGradient(
        listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondaryContainer,
            Color.Transparent
        )
    )
    val bannerBrush = Brush.composite(backBrush, frontBrush, blendMode = BlendMode.Darken)
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(brush = bannerBrush)
            .fillMaxWidth()
            .height(220.dp)
    ) {
        val infinitelyScrollableCount = Int.MAX_VALUE
        val startIndex = Int.MAX_VALUE / 2
        val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { infinitelyScrollableCount })
        val scope = rememberCoroutineScope()
        val cardWidth = 350.dp
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(cardWidth),
            snapPosition = SnapPosition.Center,
            userScrollEnabled = false,
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-24).dp),
        ) { page ->
            val realIndex = page % products.size
            val product = products[realIndex]
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue.coerceIn(0f, 1f)

            val scale = 1f - 0.2f * pageOffset
            val alpha = 1f - 0.3f * pageOffset


            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .height(150.dp)
                    .graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.alpha = alpha
                    }
                    .pointerHoverIcon(if(page == pagerState.currentPage) PointerIcon.Hand else PointerIcon.Default)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (page == pagerState.currentPage){
                                onProductClick(product)
                            }
                        }
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                CustomAsyncImage(
                    url = "${BASE_URL}${product.imageUrl}",
                    contentDescription = product.description,
                    isFullSize = true
                )
            }
        }
        IconButton(
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
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
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 20.dp)
                .pointerHoverIcon(icon = PointerIcon.Hand),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.9f
                )
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "Next Product Card",
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = bannerTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopStart).padding(start = 12.dp, top = 12.dp)
        )
    }
}