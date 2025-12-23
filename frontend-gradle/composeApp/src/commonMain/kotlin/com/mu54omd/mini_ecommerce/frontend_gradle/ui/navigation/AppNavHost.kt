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
import androidx.compose.runtime.DisposableEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.SearchBarState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.AppDrawer
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.FAB
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.NavigationBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.Screen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components.TopBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.login.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.OrdersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.ProductsScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.splash.SplashScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.UsersScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme
import kotlinx.coroutines.delay
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
    var isNavReady by remember { mutableStateOf(false) }

    val authState by authViewModel.state.collectAsState()
    val authEffect = authViewModel.effect
    val cartState = cartViewModel.state.collectAsState().value

    var isCartEmpty by remember { mutableStateOf(true) }
    LaunchedEffect(cartState) {
            isCartEmpty = cartState.isEmpty
    }

    val navigationDestination = when (authState.currentUser.role) {
        UserRole.ADMIN -> listOf(Screen.Products, Screen.Users, Screen.Orders, Screen.Admin)
        UserRole.USER -> listOf(
            Screen.Products,
            if (isCartEmpty) Screen.Cart else Screen.FullCart,
            Screen.Orders
        )

        else -> listOf(Screen.Products)
    }

    var selectedDestination by rememberSaveable(authState.token) {
        mutableIntStateOf(
            navigationDestination.indices.first
        )
    }

    val startDestination = Screen.Login.route
    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: startDestination
    val isLogin = currentDestination == Screen.Login.route

    var isFirstLaunch by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit){
        if(isFirstLaunch){
            delay(2000)
            isFirstLaunch = false
        }
    }

    LaunchedEffect(isNavReady){
        if (!isNavReady) return@LaunchedEffect
        authEffect.collect { effect ->
            when(effect){
                is AuthUiEffect.NavigateToHome -> {
                    cartViewModel.loadCart()
                    navController.navigate(navigationDestination.first().route) {
                        popUpTo(navigationDestination.first().route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is AuthUiEffect.NavigateToHomeAsGuest -> {
                    authViewModel.clearToken()
                    navController.navigate(navigationDestination.first().route) {
                        popUpTo(navigationDestination.first().route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is AuthUiEffect.LogOut -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(startDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                else -> Unit
            }
        }
    }

    LaunchedEffect(authState.isStoredTokenValid) {
        if (!authState.isStoredTokenValid) {
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
        val isNarrowScreen by remember(maxWidth, maxHeight) {
            derivedStateOf { maxHeight < 400.dp || maxWidth < 250.dp }
        }
        val isCompactScreen by remember(maxWidth) {
            derivedStateOf { maxWidth < 450.dp }
        }
        var isMainMenuHidden by remember { mutableStateOf(true) }
        var addProductModalState by remember { mutableStateOf(false) }
        var addUserModalState by remember { mutableStateOf(false) }

        var isDrawerVisible by remember { mutableStateOf(false) }

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
                                onSearchQuery = { query -> productViewModel.onSearchQueryChanged(query.ifBlank { null }) },
                                onClearSearchQuery = { productViewModel.onSearchQueryChanged(null) }
                            )
                        }

                        Screen.Orders.route -> {
                            SearchBarState(
                                isVisible = authState.currentUser.role == UserRole.ADMIN,
                                placeHolderText = "Search Orders",
                                onSearchQuery = { query -> orderViewModel.onSearchQueryChanged(query = query) },
                                onClearSearchQuery = { orderViewModel.onSearchQueryChanged(query = null) }
                            )
                        }

                        else -> SearchBarState()
                    },
                    onMainMenuClick = { isMainMenuHidden = !isMainMenuHidden },
                    onMainMenuDismiss = { isMainMenuHidden = true },
                    onToggleTheme = onToggleTheme,
                    onLogoutClick = {
                        authViewModel.logout(null)
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        selectedDestination = navigationDestination.indices.first
                    },
                    onDrawerClick = { isDrawerVisible = true }
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
                    modifier = Modifier.padding(start = if (isWideScreen && !isLogin) 100.dp else 0.dp)
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            authViewModel = authViewModel,
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
                            onExit = { error -> authViewModel.logout(error) }
                        )
                    }
                    composable(Screen.Orders.route) {
                        OrdersScreen(
                            isCompact = isCompactScreen,
                            orderViewModel = orderViewModel,
                            userRole = authState.currentUser.role,
                            onExit = { error -> authViewModel.logout(error) }
                        )
                    }
                    composable(Screen.Products.route) {
                        ProductsScreen(
                            isWideScreen = isWideScreen,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            userRole = authState.currentUser.role,
                            addProductModalState = addProductModalState,
                            onAddProductModalChange = { state -> addProductModalState = state },
                            onExit = { error -> authViewModel.logout(error) }
                        )
                    }
                    composable(Screen.Cart.route) {
                        CartScreen(
                            cartViewModel = cartViewModel,
                            onExit = { error -> authViewModel.logout(error) }
                        )
                    }
                }
                DisposableEffect(navController) {
                    val listener = NavController.OnDestinationChangedListener { _, _, _ ->
                        isNavReady = true
                    }
                    navController.addOnDestinationChangedListener(listener)
                    onDispose {
                        navController.removeOnDestinationChangedListener(listener)
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
                userRole = authState.currentUser.role,
                isWideScreen = isWideScreen,
                currentDestination = currentDestination,
                onFabClick = {
                    if (currentDestination == Screen.Products.route) {
                        addProductModalState = true
                    } else if (currentDestination == Screen.Users.route) {
                        addUserModalState = true
                    }
                }
            )
        }
        AppDrawer(
            isDarkTheme = isDarkTheme,
            isDrawerVisible = isDrawerVisible,
            username = authState.currentUser.username,
            email = authState.currentUser.email,
            onDismiss = { isDrawerVisible = false }
        )
        SplashScreen(
            isDarkTheme = isDarkTheme,
            isSplashVisible = isFirstLaunch
        )
    }
}


