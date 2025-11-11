package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.Constants.BASE_URL


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductDetails(
    product: Product,
    onDismiss: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(5))
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .clickable{
                    onDismiss()
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(100))
                        .size(100.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "image_${product.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                ) {
                    CustomAsyncImage(
                        url = "$BASE_URL${product.imageUrl}",
                        contentDescription = product.description,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = product.name,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "name_${product.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                    Text(
                        text = product.price.toString(),
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "price_${product.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                    Text(
                        text = product.stock.toString(),
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "stock_${product.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.description,
                )
                Text(
                    text = "This is a test text. This is a test text. This is a test text." +
                            " This is a test text. This is a test text. This is a test text. " +
                            "This is a test text. This is a test text. This is a test text. " +
                            "This is a test text. This is a test text. This is a test text. ",
                )
            }
        }
    }
}