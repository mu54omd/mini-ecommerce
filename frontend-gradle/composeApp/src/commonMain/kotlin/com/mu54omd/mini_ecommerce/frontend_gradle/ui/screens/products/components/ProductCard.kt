package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
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
        Card(
            modifier = modifier,
            interactionSource = interactionSource,
            onClick = onProductClick,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            var addedItem by rememberSaveable { mutableIntStateOf(addedItem) }
            Box(
                Modifier.weight(0.7f), Alignment.Center
            ) {
                CustomAsyncImage(
                    url = $$"$$BASE_URL$${product.imageUrl}",
                    contentDescription = product.description,
                    errorTint = MaterialTheme.colorScheme.error,
                    size = 150.dp,
                    modifier = Modifier
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .height(70.dp)
                            then (
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
                        .graphicsLayer { alpha = 0.7f }
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright
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

