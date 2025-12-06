package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCards(
    lazyGridState: LazyGridState = rememberLazyGridState(),
    latestProductsBannerState: State<UiState<List<Product>>>,
    productsState: State<UiState<List<Product>>>,
    categoriesState: State<UiState<List<String>>>,
    cartItems: Map<Long, Int>,
    userRole: UserRole,
    enableSharedTransition: Boolean = true,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    onProductClick: (Product) -> Unit,
    onEditClick: (Product) -> Unit,
    onRemoveClick: (Long) -> Unit,
    onIncreaseItem: (Long) -> Unit,
    onDecreaseItem: (Long) -> Unit,
    onExit: (UiState<*>) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column {
        when (latestProductsBannerState.value) {
            is UiState.Idle -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                ProductBanner(
                    bannerTitle = "Latest Products",
                    products = (latestProductsBannerState.value as UiState.Success<List<Product>>).data
                )
            }

            else -> {
                onExit(latestProductsBannerState.value)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        CategoryCustomChips(
            categoriesState = categoriesState,
            selectedCategory = selectedCategory,
            onSelectCategory = { category -> onSelectCategory(category) },
            onExit = onExit
        )
        when (productsState.value) {
            is UiState.Idle -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                if ((productsState.value as UiState.Success<List<Product>>).data.isEmpty()) {
                    EmptyPage("Oops!", "No product found!")
                } else {
                    val products = (productsState.value as UiState.Success<List<Product>>).data
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(contentAlignment = Alignment.Center) {
                        LazyVerticalGrid(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            columns = GridCells.Adaptive(180.dp),
                            contentPadding = PaddingValues(bottom = 50.dp),
                            state = lazyGridState
                        ) {
                            items(items = products, key = { product -> product.id!! }) { product ->

                                val interaction = remember { MutableInteractionSource() }
                                val isHovered by interaction.collectIsHoveredAsState()
                                val isPressed by interaction.collectIsPressedAsState()

                                val isActive = isHovered || isPressed

                                val scale by animateFloatAsState(
                                    targetValue = if (isActive) 1.1f else 1f,
                                    animationSpec = tween(durationMillis = 150),
                                    label = ""
                                )
                                ProductCard(
                                    modifier = Modifier.size(180.dp).padding(8.dp).animateItem(),
                                    addedItem = cartItems[product.id] ?: 0,
                                    scale = scale,
                                    interactionSource = interaction,
                                    product = product,
                                    userRole = userRole,
                                    onProductClick = { onProductClick(product) },
                                    onIncreaseItem = { onIncreaseItem(product.id!!) },
                                    onDecreaseItem = { onDecreaseItem(product.id!!) },
                                    onEditClick = { onEditClick(product) },
                                    onRemoveClick = { onRemoveClick(product.id!!) },
                                    enableSharedTransition = enableSharedTransition,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )

                            }
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.background,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                            Color.Transparent,
                                        )
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                )
                        )
                    }
                }
            }

            else -> {
                onExit(productsState.value)
            }
        }
    }
}
