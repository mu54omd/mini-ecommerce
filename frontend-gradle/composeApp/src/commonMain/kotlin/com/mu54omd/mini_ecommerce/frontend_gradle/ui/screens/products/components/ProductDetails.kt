package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductDetails(
    product: Product,
    addedItem: Int,
    userRole: UserRole,
    alignment: Alignment = Alignment.Center,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onIncreaseItem: () -> Unit,
    onDecreaseItem: () -> Unit,
    enableSharedTransition: Boolean = true,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(animatedVisibilityScope) {
        with(sharedTransitionScope) {
            var addedItem by rememberSaveable { mutableIntStateOf(addedItem) }
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
                    .background(color = Color.Transparent)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .widthIn(min = 350.dp, max = 450.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceBright)
                        .verticalScroll(state = rememberScrollState())
                            then (
                            if (enableSharedTransition) {
                                Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "container_${product.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                                    )
                            } else {
                                Modifier
                            }
                            )

                ) {
                    CustomAsyncImage(
                        url = "$BASE_URL${product.imageUrl}",
                        isFullSize = true,
                        contentDescription = product.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                                then (
                                if (enableSharedTransition) {
                                    Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState(key = "image_${product.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                } else {
                                    Modifier
                                }
                                )
                            .clip(
                                RoundedCornerShape(
                                    topStart = 10.dp,
                                    bottomStart = 10.dp,
                                    topEnd = 10.dp,
                                    bottomEnd = 10.dp
                                )
                            ),
                        errorTint = MaterialTheme.colorScheme.error,
                    )

                    Row(
                        modifier = Modifier
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
                            )
                            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                            .fillMaxWidth()
                                then (
                                if (enableSharedTransition) {
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = "product_info${product.id}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                } else {
                                    Modifier
                                }
                                ),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,

                            ) {
                            Text(
                                product.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "${product.price} $",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "#${product.stock}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
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
                    HorizontalDivider()
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.composite(
                                    dstBrush = verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.primaryContainer
                                        )
                                    ),
                                    srcBrush = verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ),
                                    blendMode = BlendMode.Color
                                ),
                            )
                            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                            .fillMaxWidth()
                            .skipToLookaheadSize()
                            .animateEnterExit(
                                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                            )
                    ) {
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = """
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam dapibus erat ac magna ultricies facilisis. Fusce lectus dolor, scelerisque et porta sit amet, molestie et purus. Phasellus hendrerit leo ac urna finibus, sit amet vestibulum risus tincidunt. Cras pellentesque vehicula dui sit amet pellentesque. Sed ultricies orci vitae hendrerit pellentesque. Cras et elit massa. Fusce id tempor nibh. Vestibulum scelerisque vestibulum risus, non auctor eros iaculis eu. Mauris a enim a dui lacinia euismod quis in metus. Quisque porttitor interdum maximus. Donec accumsan, mauris non convallis sodales, leo elit consectetur mauris, nec laoreet sapien quam a dui.
                                Quisque faucibus eget velit commodo bibendum. Nullam ut enim sodales, sagittis metus sed, fringilla ex. Maecenas tempor lectus sed lacus feugiat, ut venenatis nulla tincidunt. Etiam facilisis justo vel auctor tempor. In tincidunt augue quis tincidunt scelerisque. Ut et nisl id massa venenatis tincidunt. Proin mattis varius libero. Aliquam in eros in lacus feugiat aliquet. In eleifend consequat tortor nec sodales. Vestibulum molestie diam quis imperdiet imperdiet. Aliquam euismod eros a turpis aliquet, et malesuada dolor commodo.
                                Donec fringilla felis a justo egestas posuere. Etiam bibendum, quam in iaculis tristique, mauris nisl fringilla purus, aliquet sollicitudin urna augue vitae diam. In tincidunt nisl id arcu aliquet, a semper leo auctor. Vivamus sed est nisi. Fusce molestie laoreet tortor, sit amet ornare tortor egestas eget. Vivamus faucibus ex mauris, eu aliquam odio viverra at. Pellentesque fermentum nisi at mi tincidunt laoreet. Vivamus eu bibendum nulla. In posuere tortor sit amet metus posuere viverra. Duis eu tincidunt nunc.
                                Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Donec quis eros varius, imperdiet ipsum nec, viverra ex. Etiam purus sem, tempus eu dui in, semper ultrices quam. Etiam tincidunt risus eleifend, placerat massa id, condimentum magna. Fusce a lacus leo. Pellentesque metus sem, ornare non odio et, bibendum pretium ipsum. Pellentesque lacus mauris, interdum eu laoreet eu, suscipit id ante.
                                Vestibulum et elit lectus. Nullam id tellus arcu. Vestibulum commodo ante id nulla interdum dapibus. Curabitur velit arcu, rutrum eu massa nec, scelerisque volutpat leo. Cras fermentum lectus nisl, eget viverra sapien condimentum et. Interdum et malesuada fames ac ante ipsum primis in faucibus. Quisque ut rhoncus magna. Nullam posuere est ut arcu sagittis volutpat.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }

            }
        }
    }
}