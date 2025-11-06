package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.NavigationRailDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AdminViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user.HomeScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user.MyOrdersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.OrdersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.ProductsScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.UsersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.ProductSearchBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel>(),
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
    adminViewModel: AdminViewModel = koinViewModel<AdminViewModel>()
) {
    val navController = rememberNavController()
    val tokenState = authViewModel.tokenState.collectAsState().value
    val userState = authViewModel.userState.collectAsState().value
    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItemCount by remember(cartState) { derivedStateOf { if (cartState is UiState.Success) cartState.data.items.size else 0 } }

    val navigationDestination = when (userState.role) {
        "ADMIN" -> listOf(Screen.Products, Screen.Users, Screen.Orders, Screen.Admin)
        "USER" -> listOf(Screen.Home, Screen.Cart, Screen.MyOrders)
        else -> listOf(Screen.Home)
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


    var selectedDestination by rememberSaveable { mutableIntStateOf(navigationDestination.indices.first) }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(tokenState) {
        if (tokenState is UiState.Error || tokenState is UiState.Unauthorized) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
    }
    Scaffold(
        bottomBar = {
            BoxWithConstraints {
                val isDesktop = maxWidth > 840.dp
                if (!isDesktop && currentDestination != Screen.Login.route) {
                    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                        navigationDestination.forEachIndexed { index, destination ->
                            NavigationBarItem(
                                selected = selectedDestination == index,
                                onClick = {
                                    navController.navigate(route = destination.route)
                                    selectedDestination = index
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
                                }
                            )
                        }
                    }
                }
            }
        },
        topBar = {
            if (currentDestination != Screen.Login.route) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(64.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentDestination?.uppercase() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.width(100.dp).padding(end = 2.dp),
                    )
                    AnimatedVisibility(
                        visible = currentDestination == Screen.Home.route || currentDestination == Screen.Products.route,
                        enter = scaleIn(tween()) + fadeIn(),
                        exit = scaleOut(tween()) + fadeOut(),
                        modifier = Modifier.weight(0.4f)
                    ) {
                        ProductSearchBar(
                            onQuery = { query ->
                                if (currentDestination == Screen.Home.route) productViewModel.filterProducts(
                                    query
                                ) else adminViewModel.filterProducts(query)
                            },
                            onClearQuery = { if (currentDestination == Screen.Home.route) productViewModel.loadProducts() else adminViewModel.getAllProducts() }
                        )
                    }
                    TextButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                            selectedDestination = navigationDestination.indices.first
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Logout", overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }
                }
            }
        }
    ) { contentPadding ->
        BoxWithConstraints {
            val isDesktop = maxWidth > 840.dp
            Row(
                modifier = Modifier.padding(contentPadding)
            ) {
                if(isDesktop && currentDestination != Screen.Login.route){
                    NavigationRail {
                        navigationDestination.forEachIndexed { index, destination ->
                            NavigationRailItem(
                                selected = selectedDestination == index,
                                onClick = {
                                    navController.navigate(destination.route)
                                    selectedDestination = index
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
                                }
                            )
                        }
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route,
                    enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
                    exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = {
                                productViewModel.reset()
                                cartViewModel.reset()
                                orderViewModel.reset()
                                cartViewModel.refresh()
                                navController.navigate(navigationDestination.first().route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            onLoginAsGuest = {
                                authViewModel.clearToken()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Admin.route) {
                        AdminPanelScreen(
                            adminViewModel = adminViewModel,
                        )
                    }

                    composable(Screen.Users.route) {
                        UsersScreen(
                            adminViewModel = adminViewModel,
                            onExit = { state ->
                                authViewModel.logout(state)
                            }
                        )
                    }

                    composable(Screen.Orders.route) {
                        OrdersScreen(
                            adminViewModel = adminViewModel,
                            onExit = { state -> authViewModel.logout(state) }
                        )
                    }

                    composable(Screen.Products.route) {
                        ProductsScreen(
                            adminViewModel = adminViewModel,
                            onExit = { state -> authViewModel.logout(state) }
                        )
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            onExit = { state ->
                                authViewModel.logout(state)
                            }
                        )
                    }

                    composable(Screen.Cart.route) {
                        CartScreen(
                            cartViewModel = cartViewModel,
                            onExit = { state ->
                                authViewModel.logout(state)
                            },
                            onConfirmClick = {
                                cartViewModel.checkout()
                                orderViewModel.load()
                                selectedDestination = 2
                                navController.navigate(Screen.MyOrders.route) {
                                    popUpTo(Screen.Home.route)
                                }
                            }
                        )
                    }

                    composable(Screen.MyOrders.route) {
                        MyOrdersScreen(
                            orderViewModel = orderViewModel,
                            onExit = { state -> authViewModel.logout(state) }
                        )
                    }
                }
            }
        }
    }
}
