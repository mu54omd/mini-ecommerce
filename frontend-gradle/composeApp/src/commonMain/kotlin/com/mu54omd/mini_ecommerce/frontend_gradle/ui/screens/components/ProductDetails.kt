package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.zIndex
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.Constants.BASE_URL


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductDetails(
    product: Product,
    onDismiss: () -> Unit,
    enableSharedTransition: Boolean = true,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(animatedVisibilityScope) {
        with(sharedTransitionScope) {
            Box(
                contentAlignment = Alignment.Center,
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
                        then(
                            if(enableSharedTransition){
                                Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "container_${product.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                                    )
                            }else{
                                Modifier
                            }
                        )

                ) {
                    CustomAsyncImage(
                        url = "$BASE_URL${product.imageUrl}",
                        contentDescription = product.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            then(
                                if(enableSharedTransition){
                                    Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState(key = "image_${product.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                }else{
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
                        errorTint = MaterialTheme.colorScheme.surface,
                        size = 250.dp,
                        )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                            then(
                                if(enableSharedTransition){
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = "product_info${product.id}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                }else{
                                    Modifier
                                }
                            )
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
                    HorizontalDivider()
                    Column(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                            .fillMaxWidth()
                            .skipToLookaheadSize()
                            .animateEnterExit(
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically()
                            )
                    ) {
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                                    "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
                                    "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla " +
                                    "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa" +
                                    " qui officia deserunt mollit anim id est laborum.",
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