package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState


enum class ProductListState {
    Cards,
    Details,
    CardsWithDetails
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductList(
    isWideScreen: Boolean = false,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    userRole: UserRole,
    latestProductsBannerState: State<UiState<List<Product>>>,
    productsState: State<UiState<List<Product>>>,
    categoriesState: State<UiState<List<String>>>,
    selectedCategory: State<String?>,
    onSelectCategory: (String?) -> Unit,
    cartItems: Map<Long, Int>,
    onEditClick: (Product) -> Unit,
    onRemoveClick: (Long) -> Unit,
    onIncreaseItem: (Long) -> Unit,
    onDecreaseItem: (Long) -> Unit,
    onExit: (UiState<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    var productListState by remember(isWideScreen) {
        mutableStateOf(if(isWideScreen) ProductListState.CardsWithDetails else ProductListState.Cards)
    }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = productListState,
            transitionSpec = {
                fadeIn(animationSpec = tween(50)) togetherWith fadeOut(animationSpec = tween(50))
            }
        ) { state ->
            when (state) {
                ProductListState.Cards -> {
                    ProductCards(
                        lazyGridState = lazyGridState,
                        latestProductsBannerState = latestProductsBannerState,
                        productsState = productsState,
                        categoriesState = categoriesState,
                        selectedCategory = selectedCategory.value,
                        onSelectCategory = onSelectCategory,
                        cartItems = cartItems,
                        userRole = userRole,
                        onProductClick = { product ->
                            selectedProduct = product
                            productListState = ProductListState.Details
                        },
                        onEditClick = onEditClick,
                        onRemoveClick = onRemoveClick,
                        onIncreaseItem = onIncreaseItem,
                        onDecreaseItem = onDecreaseItem,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onExit = onExit
                    )
                }

                ProductListState.Details -> {
                    selectedProduct?.let { product ->
                        ProductDetails(
                            product = product,
                            addedItem = cartItems[product.id] ?: 0,
                            userRole = userRole,
                            onDismiss = {
                                productListState = ProductListState.Cards
                            },
                            onIncreaseItem = { onIncreaseItem(product.id!!) },
                            onDecreaseItem = { onDecreaseItem(product.id!!) },
                            onEditClick = { onEditClick(product) },
                            onRemoveClick = { onRemoveClick(product.id!!) },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                    }
                }

                ProductListState.CardsWithDetails -> {
                    var isDetailsBoxVisible by remember { mutableStateOf(false) }
                    val filteredProductsState = remember(selectedProduct, productsState.value) {
                        derivedStateOf {
                            val original = productsState.value
                            if (isDetailsBoxVisible && selectedProduct != null) {
                                when (original) {
                                    is UiState.Success -> UiState.Success(
                                        original.data.filter { it.id != selectedProduct!!.id }
                                    )
                                    else -> original
                                }
                            } else {
                                original
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            ProductCards(
                                lazyGridState = lazyGridState,
                                latestProductsBannerState = latestProductsBannerState,
                                productsState = filteredProductsState,
                                categoriesState = categoriesState,
                                selectedCategory = selectedCategory.value,
                                onSelectCategory = onSelectCategory,
                                cartItems = cartItems,
                                userRole = userRole,
                                enableSharedTransition = false,
                                onProductClick = { product ->
                                    isDetailsBoxVisible = true
                                    selectedProduct = product
                                },
                                onEditClick = onEditClick,
                                onRemoveClick = onRemoveClick,
                                onIncreaseItem = onIncreaseItem,
                                onDecreaseItem = onDecreaseItem,
                                onExit = onExit,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent,
                            )
                        }
                        AnimatedVisibility(
                            visible = isDetailsBoxVisible,
                            enter = slideInHorizontally(
                                animationSpec = tween(100),
                                initialOffsetX = { it }
                            ) + fadeIn(animationSpec = tween(100)),
                            exit = slideOutHorizontally(
                                animationSpec = tween(durationMillis = 50),
                                targetOffsetX = { it }
                            ) + fadeOut(animationSpec = tween(50))
                        ) {
                            selectedProduct?.let { product ->
                                ProductDetails(
                                    product = product,
                                    alignment = Alignment.CenterEnd,
                                    addedItem = cartItems[product.id] ?: 0,
                                    userRole = userRole,
                                    onIncreaseItem = { onIncreaseItem(product.id!!) },
                                    onDecreaseItem = { onDecreaseItem(product.id!!) },
                                    onEditClick = { onEditClick(product) },
                                    onRemoveClick = { onRemoveClick(product.id!!) },
                                    onDismiss = { isDetailsBoxVisible = false },
                                    enableSharedTransition = false,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}