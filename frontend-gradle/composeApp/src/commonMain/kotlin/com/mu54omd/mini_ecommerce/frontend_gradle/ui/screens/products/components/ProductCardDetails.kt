package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.AppThemeExtras
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCardDetails(
    product: Product,
    userRole: UserRole,
    itemCount: Int,
    isLarge: Boolean = false,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onDecreaseItem: () -> Unit,
    onIncreaseItem: () -> Unit,
    enableSharedTransition: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
){
    with(sharedTransitionScope) {
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
                        brush = AppThemeExtras.brushes.cardBrush,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    ),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    product.name,
                    fontWeight = FontWeight.Bold,
                    style = if(isLarge) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().background(
                        color = ExtendedTheme.colorScheme.quinary.colorContainer.copy(alpha = 0.5f)
                    ).padding(top = 4.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "${product.price} $",
                        style = if(isLarge) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "#${product.stock}",
                        style = if(isLarge) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (userRole == UserRole.USER) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.4f)
                                .clickable(
                                    enabled = itemCount > 0,
                                ){
                                    onDecreaseItem()
                                }
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                        ){
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Remove Product from Cart",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "$itemCount",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(0.3f)
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.4f)
                                .clickable(
                                    enabled = (itemCount < product.stock) && (product.stock > 0),
                                ){
                                    onIncreaseItem()
                                }
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Product to Cart",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    } else if (userRole == UserRole.ADMIN) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    enabled = true
                                ){
                                    onRemoveClick()
                                }
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Product",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    enabled = true,
                                ){
                                    onEditClick()
                                }
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Product",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}