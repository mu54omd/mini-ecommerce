package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.HomeScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.OrdersScreen
import org.koin.compose.viewmodel.koinViewModel
import kotlin.compareTo

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

    val bottomBarDestinations = when(userState.role) {
        "ADMIN" -> listOf(Screen.Home, Screen.Cart, Screen.Orders, Screen.Admin)
        "USER" -> listOf(Screen.Home, Screen.Cart, Screen.Orders)
        else -> listOf(Screen.Home)
    }

    fun getCartIcon(): ImageVector {
        return if (cartItemCount > 0) {
            Icons.Default.ShoppingCartCheckout
        } else {
            Icons.Default.ShoppingCart
        }
    }

    var selectedDestination by rememberSaveable { mutableIntStateOf(bottomBarDestinations.indices.first) }
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
            if (currentDestination != Screen.Login.route) {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    bottomBarDestinations.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedDestination == index,
                            onClick = {
                                when(destination){
                                    Screen.Cart -> { cartViewModel.refresh() }
                                    Screen.Home -> { cartViewModel.refresh() }
                                    Screen.Login -> {}
                                    Screen.Orders -> { orderViewModel.load() }
                                    Screen.Admin -> {}
                                }
                                navController.navigate(route = destination.route)
                                selectedDestination = index
                            },
                            label = {
                                Text(
                                    text = destination.label,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            icon = {
                                val iconToShow = if (destination == Screen.Cart) getCartIcon() else destination.icon
                                Icon(
                                    imageVector = iconToShow,
                                    contentDescription = destination.contentDescription
                                )
                            }
                        )
                    }
                }
            }
        },
        topBar = {
            if(currentDestination != Screen.Login.route) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentDestination?.uppercase() ?: "",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                            selectedDestination = bottomBarDestinations.indices.first
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Logout")
                    }
                }
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        productViewModel.reset()
                        cartViewModel.reset()
                        orderViewModel.reset()
                        cartViewModel.refresh()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onLoginAsGuest = {
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
                        navController.navigate(Screen.Orders.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.Orders.route) {
                OrdersScreen(
                    orderViewModel = orderViewModel,
                    onExit = { state -> authViewModel.logout(state) }
                )
            }
        }
    }
}
