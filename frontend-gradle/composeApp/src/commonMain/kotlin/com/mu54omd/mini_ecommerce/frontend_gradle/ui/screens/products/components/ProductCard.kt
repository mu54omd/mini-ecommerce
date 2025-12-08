package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage
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
    addedItem: Int,
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

        var addedItem by rememberSaveable { mutableIntStateOf(addedItem) }
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
                errorTint = MaterialTheme.colorScheme.error,
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (enableSharedTransition) {
                            Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(
                                        key = "product_info${product.id}"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        } else {
                            Modifier
                        }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            brush = Brush.composite(
                                dstBrush = verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                ),
                                srcBrush = verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ),
                                blendMode = BlendMode.Color
                            ),
                            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                        )
                        .padding(4.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        product.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${product.price} $",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "#${product.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    if (userRole == UserRole.USER) {
                        IconButton(
                            onClick = {
                                addedItem--
                                onDecreaseItem()
                            },
                            enabled = addedItem > 0,
                            modifier = Modifier.pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Remove Product from Cart"
                            )
                        }
                        Text(text = "$addedItem")
                        IconButton(
                            onClick = {
                                addedItem++
                                onIncreaseItem()
                            },
                            enabled = (addedItem < product.stock) && (product.stock > 0),
                            modifier = Modifier.pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Product to Cart"
                            )
                        }
                    } else if (userRole == UserRole.ADMIN) {
                        IconButton(
                            onClick = onRemoveClick,
                            enabled = true,
                            modifier = Modifier.pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Product"
                            )
                        }
                        IconButton(
                            onClick = {
                                onEditClick()
                            },
                            enabled = true,
                            modifier = Modifier.pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Product"
                            )
                        }
                    }
                }
            }
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
                            addedItem = 3,
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
                            addedItem = 3,
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



