package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.CustomAsyncImage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 150.dp,
    cardHeight: Dp = 150.dp,
    imageWidth: Dp = 100.dp,
    interactionSource: MutableInteractionSource? = null,
    scale: Float,
    itemCount: Int,
    product: Product,
    userRole: UserRole,
    onProductClick: () -> Unit,
    onIncreaseItem: () -> Unit,
    onDecreaseItem: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
    enableSharedTransition: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .width(width = cardWidth)
                .height(height = cardHeight)
                .padding(4.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onProductClick()
                }
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            CustomAsyncImage(
                url = $$"$$BASE_URL$${product.imageUrl}",
                contentDescription = product.description,
                errorTint = MaterialTheme.colorScheme.errorContainer,
                loadedImageSize = imageWidth,
                errorImageSize = imageWidth,
                loadingImageSize = imageWidth,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-20).dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .then(
                        if (enableSharedTransition) {
                            Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    key = "image_${product.id}"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(10.dp))
            )
            ProductCardDetails(
                product = product,
                userRole = userRole,
                itemCount = itemCount,
                onEditClick = onEditClick,
                onRemoveClick = onRemoveClick,
                onDecreaseItem = onDecreaseItem,
                onIncreaseItem = onIncreaseItem,
                enableSharedTransition = enableSharedTransition,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun ProductCardPreview() {
    MiniECommerceTheme {
        val product = Product(
            id = 120L,
            name = "Product",
            category = "Smartphone",
            description = "The newest smartphone in the world",
            price = 1230.25,
            stock = 5,
        )
        Surface {
            SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(targetState = true) { state ->
                    Row {
                        ProductCard(
                            cardWidth = 150.dp,
                            cardHeight = 200.dp,
                            scale = 1f,
                            itemCount = 3,
                            product = product,
                            userRole = UserRole.USER,
                            onProductClick = {},
                            onIncreaseItem = {},
                            onDecreaseItem = {},
                            onEditClick = {},
                            onRemoveClick = {},
                            enableSharedTransition = false,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )
                        ProductCard(
                            cardWidth = 180.dp,
                            cardHeight = 220.dp,
                            scale = 1f,
                            itemCount = 3,
                            product = product,
                            userRole = UserRole.ADMIN,
                            onProductClick = {},
                            onIncreaseItem = {},
                            onDecreaseItem = {},
                            onEditClick = {},
                            onRemoveClick = {},
                            enableSharedTransition = false,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    }
                }
            }
        }
    }
}



