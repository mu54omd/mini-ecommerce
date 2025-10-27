package com.mu54omd.mini_ecommerce.frontend_gradle.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CheckoutScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.HomeScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.OrdersScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel>(),
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
) {
    val navController = rememberNavController()
    val loginState = authViewModel.loginState.collectAsState().value
    LaunchedEffect(loginState) {
        if (loginState is UiState.Error || loginState is UiState.Unauthorized) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
        exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) }
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
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onCartClick = {
                    cartViewModel.refresh()
                    navController.navigate(Screen.Cart.route)
                              },
                onOrderClick = {
                    orderViewModel.load()
                    navController.navigate(Screen.Orders.route)
                               },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onExit = { state ->
                    authViewModel.logout(state)
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckoutClick = { navController.navigate(Screen.Checkout.route) },
                onBack = { navController.popBackStack() },
                onExit = { state ->
                    authViewModel.logout(state)
                }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                onConfirmClick = {
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onCancelClick = {}
            )
        }

        composable(Screen.Orders.route) {
            OrdersScreen(
                orderViewModel = orderViewModel,
                onBack = { navController.popBackStack() },
                onExit = { state -> authViewModel.logout(state) }
            )
        }
    }
}
