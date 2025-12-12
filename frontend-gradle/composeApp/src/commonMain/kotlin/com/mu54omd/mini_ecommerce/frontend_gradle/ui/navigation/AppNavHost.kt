package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.FAB
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.NavigationBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.Screen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.SearchBarState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.TopBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.login.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.OrdersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.ProductsScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.UsersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel>(),
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
    userViewModel: UserViewModel = koinViewModel<UserViewModel>(),
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit,
) {
    val navController = rememberNavController()
    val tokenState = authViewModel.tokenState.collectAsState().value
    val userState = authViewModel.userState.collectAsState().value
    val cartState = cartViewModel.cartState.collectAsState().value

    var isCartEmpty by remember { mutableStateOf(true) }
    LaunchedEffect(cartState) {
        if (cartState is UiState.Success) {
            isCartEmpty = cartState.data.items.isEmpty()
        }
    }

    val navigationDestination = when (userState.role) {
        UserRole.ADMIN -> listOf(Screen.Products, Screen.Users, Screen.Orders, Screen.Admin)
        UserRole.USER -> listOf(Screen.Products, if(isCartEmpty) Screen.Cart else Screen.FullCart, Screen.Orders)
        else -> listOf(Screen.Products)
    }

    var selectedDestination by rememberSaveable(tokenState) {
        mutableIntStateOf(
            navigationDestination.indices.first
        )
    }

    val startDestination = Screen.Login.route
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route ?: startDestination
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
        val isNarrowScreen by remember(maxWidth, maxHeight){
            derivedStateOf { maxHeight < 400.dp || maxWidth < 250.dp}
        }
        val isCompactScreen by remember(maxWidth) {
            derivedStateOf { maxWidth < 450.dp }
        }
        var isMainMenuHidden by remember { mutableStateOf(true) }
        var addProductModalState by remember { mutableStateOf(false) }
        var addUserModalState by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopBar(
                    isLogin = isLogin,
                    isMainMenuHidden = isMainMenuHidden,
                    isDarkTheme = isDarkTheme,
                    searchBarState = when (currentDestination) {
                        Screen.Products.route -> {
                            SearchBarState(
                                isVisible = true,
                                placeHolderText = "Search Products",
                                onSearchQuery = { query -> productViewModel . setSearchQuery (query.ifBlank { null })  },
                                onClearSearchQuery = { productViewModel.setSearchQuery(null) }
                            )
                        }
                        Screen.Orders.route -> {
                            SearchBarState(
                                isVisible = true,
                                placeHolderText = "Search Orders",
                                onSearchQuery = { query -> orderViewModel.setSearchQuery(query = query) },
                                onClearSearchQuery = { orderViewModel.setSearchQuery(query = null) }
                            )
                        }
                        else -> SearchBarState()
                    },
                    onMainMenuClick = { isMainMenuHidden = !isMainMenuHidden },
                    onMainMenuDismiss = { isMainMenuHidden = true },
                    onToggleTheme = onToggleTheme,
                    onLogoutClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        selectedDestination = navigationDestination.indices.first
                    },
                )
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier.padding(contentPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
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
                    },
                    modifier = Modifier.padding(start = if(isWideScreen && !isLogin) 100.dp else 0.dp)
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
                            addUserModalState = addUserModalState,
                            onAddUserStateChange = { state -> addUserModalState = state },
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
                            addProductModalState = addProductModalState,
                            onAddProductModalChange = { state -> addProductModalState = state},
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
                NavigationBar(
                    isLogin = isLogin,
                    isWideScreen = isWideScreen,
                    isNarrowScreen = isNarrowScreen,
                    navigationDestination = navigationDestination,
                    selectedDestination = selectedDestination,
                    onDestinationClick = { index, destination ->
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
                    barColor = MaterialTheme.colorScheme.primaryContainer,
                    circleColor = ExtendedTheme.colorScheme.quinary.colorContainer,
                    selectedColor = ExtendedTheme.colorScheme.quinary.color,
                    unselectedColor = MaterialTheme.colorScheme.secondary
                )
            }
            FAB(
                userRole = userState.role,
                isWideScreen = isWideScreen,
                currentDestination = currentDestination,
                onFabClick = {
                    if(currentDestination == Screen.Products.route) {
                        addProductModalState = true
                    } else if(currentDestination == Screen.Users.route){
                        addUserModalState = true
                    }
                }
            )
        }
    }
}


