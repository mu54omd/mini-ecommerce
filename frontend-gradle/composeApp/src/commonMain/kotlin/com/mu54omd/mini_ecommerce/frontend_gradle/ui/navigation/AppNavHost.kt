package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.login.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.OrdersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.ProductsScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.UsersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.SearchBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.extendedLight
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel>(),
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
    userViewModel: UserViewModel = koinViewModel<UserViewModel>()
) {
    val navController = rememberNavController()
    val tokenState = authViewModel.tokenState.collectAsState().value
    val userState = authViewModel.userState.collectAsState().value
    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItemCount by remember(cartState) { derivedStateOf { if (cartState is UiState.Success) cartState.data.items.size else 0 } }
    val navigationDestination = when (userState.role) {
        UserRole.ADMIN -> listOf(Screen.Products, Screen.Users, Screen.Orders, Screen.Admin)
        UserRole.USER -> listOf(Screen.Products, Screen.Cart, Screen.Orders)
        else -> listOf(Screen.Products)
    }

    val cartIcon = remember(cartItemCount) {
        if (cartItemCount > 0) {
            Icons.Filled.ShoppingCartCheckout
        } else {
            Icons.Filled.ShoppingCart
        }
    }
    val cartIconColor = if (cartItemCount > 0) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }


    var selectedDestination by rememberSaveable(tokenState) {
        mutableIntStateOf(
            navigationDestination.indices.first
        )
    }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val isLogin = currentDestination == Screen.Login.route

    LaunchedEffect(tokenState) {
        if (tokenState is UiState.Error) {
            if (currentDestination != Screen.Login.route) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
    BoxWithConstraints {
        val isWideScreen by remember(maxWidth) {
            derivedStateOf { maxWidth > 840.dp }
        }
        val isCompactScreen by remember(maxWidth) {
            derivedStateOf { maxWidth < 450.dp }
        }
        Scaffold(
            bottomBar = {
                AnimatedContent(
                    targetState = !isWideScreen,
                    transitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down
                        )
                    },
                ) { state ->
                    if (state) {
                        NavigationBar(
                            modifier = Modifier.alpha(if (isLogin) 0f else 1f),
                            windowInsets = NavigationBarDefaults.windowInsets,
                        ) {
                            navigationDestination.forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    selected = selectedDestination == index,
                                    onClick = {
                                        if (currentDestination != destination.route) {
                                            navController.navigate(route = destination.route) {
                                                launchSingleTop = true
                                                restoreState = true
                                                popUpTo(navigationDestination.first().route) {
                                                    saveState = true
                                                }
                                            }
                                            selectedDestination = index
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = destination.label,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    icon = {
                                        val iconToShow =
                                            if (destination == Screen.Cart) cartIcon else destination.icon
                                        val iconColor =
                                            if (destination == Screen.Cart) cartIconColor else MaterialTheme.colorScheme.onSurface
                                        Icon(
                                            imageVector = iconToShow,
                                            contentDescription = destination.contentDescription,
                                            tint = iconColor
                                        )
                                    },
                                    modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default),
                                    enabled = !isLogin
                                )
                            }
                        }
                    }
                }

            },
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                        .height(64.dp)
                        .graphicsLayer {
                            alpha = if (isLogin) 0f else 1f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.width(100.dp).align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = currentDestination?.uppercase() ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        when (currentDestination) {
                            Screen.Products.route -> {
                                SearchBar(
                                    placeHolderText = "Search Products",
                                    onQuery = { query -> productViewModel.setSearchQuery(query.ifBlank { null }) },
                                    onClearQuery = { productViewModel.setSearchQuery(null) },
                                )
                            }

                            Screen.Orders.route -> {
                                SearchBar(
                                    placeHolderText = "Search Orders",
                                    onQuery = { query -> orderViewModel.setSearchQuery(query = query) },
                                    onClearQuery = { orderViewModel.setSearchQuery(query = null) },
                                )
                            }
                        }
                        TextButton(
                            onClick = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                selectedDestination = navigationDestination.indices.first
                            },
                            modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default),
                            enabled = !isLogin
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                            )
                            AnimatedContent(targetState = isCompactScreen) { state ->
                                if (!state) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Logout",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { contentPadding ->

            Row(
                modifier = Modifier.padding(contentPadding)
            ) {
                AnimatedContent(
                    targetState = isWideScreen,
                    transitionSpec = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    }
                ) { state ->
                    if (state) {
                        NavigationRail(
                            modifier = Modifier.alpha(if (isLogin) 0f else 1f)
                        ) {
                            navigationDestination.forEachIndexed { index, destination ->
                                NavigationRailItem(
                                    selected = selectedDestination == index,
                                    onClick = {
                                        if (currentDestination != destination.route) {
                                            navController.navigate(destination.route) {
                                                launchSingleTop = true
                                                restoreState = true
                                                popUpTo(navigationDestination.first().route) {
                                                    saveState = true
                                                }
                                            }
                                            selectedDestination = index
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = destination.label,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    icon = {
                                        val iconToShow =
                                            if (destination == Screen.Cart) cartIcon else destination.icon
                                        val iconColor =
                                            if (destination == Screen.Cart) cartIconColor else MaterialTheme.colorScheme.onSurface
                                        Icon(
                                            imageVector = iconToShow,
                                            contentDescription = destination.contentDescription,
                                            tint = iconColor
                                        )
                                    },
                                    modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default),
                                    enabled = !isLogin
                                )
                            }
                        }
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route,
                    enterTransition = {
                        if (initialState.destination.route == Screen.Login.route) {
                            scaleIn()
                        } else {
                            fadeIn(tween(100))
                        }
                    },
                    exitTransition = {
                        if (targetState.destination.route == Screen.Login.route) {
                            fadeOut(tween(200))
                        } else {
                            fadeOut(tween(100))
                        }
                    }
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = {
                                productViewModel.resetAllStates()
                                cartViewModel.resetAllStates()
                                orderViewModel.resetAllStates()
                                cartViewModel.getCart()
                                navController.navigate(navigationDestination.first().route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            onLoginAsGuest = {
                                productViewModel.resetAllStates()
                                authViewModel.resetAllStates()
                                authViewModel.clearToken()
                                navController.navigate(navigationDestination.first().route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable(Screen.Admin.route) {
                        AdminPanelScreen()
                    }

                    composable(Screen.Users.route) {
                        UsersScreen(
                            userViewModel = userViewModel,
                            onExit = { state ->
                                authViewModel.logout(state)
                                userViewModel.resetAllStates()
                            }
                        )
                    }

                    composable(Screen.Orders.route) {
                        OrdersScreen(
                            isCompact = isCompactScreen,
                            orderViewModel = orderViewModel,
                            userRole = userState.role,
                            onExit = { state ->
                                authViewModel.logout(state)
                                orderViewModel.resetAllStates()
                            }
                        )
                    }

                    composable(Screen.Products.route) {
                        ProductsScreen(
                            isWideScreen = isWideScreen,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            userRole = userState.role,
                            onExit = { state ->
                                authViewModel.logout(state)
                                productViewModel.resetAllStates()
                            }
                        )
                    }

                    composable(Screen.Cart.route) {
                        CartScreen(
                            cartViewModel = cartViewModel,
                            onExit = { state ->
                                authViewModel.logout(state)
                                cartViewModel.resetAllStates()
                            },
                            onConfirmClick = {
                                cartViewModel.checkout()
                                orderViewModel.getUserOrders()
                                selectedDestination = 2
                                navController.navigate(Screen.Orders.route) {
                                    popUpTo(Screen.Products.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}


