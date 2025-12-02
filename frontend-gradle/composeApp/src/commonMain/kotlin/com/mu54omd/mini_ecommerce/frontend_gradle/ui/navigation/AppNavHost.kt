package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.AdminPanelScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.AnimatedNavigationBar
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.AnimatedNavigationRail
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.MainMenu
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.SearchBar
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
        var isMainMenuHidden by remember { mutableStateOf(true) }
        var addProductModalState by remember { mutableStateOf(false) }
        var addUserModalState by remember { mutableStateOf(false) }

        Scaffold(
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
                        IconButton(
                            onClick = {
                                isMainMenuHidden = !isMainMenuHidden
                            },
                            modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default),
                            enabled = !isLogin
                        ){
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Main Menu Icon"
                            )
                        }
                        MainMenu(
                            isExpanded = !isMainMenuHidden,
                            isDarkTheme = isDarkTheme,
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
                            onDismiss = { isMainMenuHidden = !isMainMenuHidden }
                        )
                    }
                }
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier.padding(contentPadding)
            ) {

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
                AnimatedContent(
                    modifier = Modifier.align(Alignment.BottomCenter).pointerInput(Unit){},
                    targetState = !isWideScreen && !isLogin,
                    transitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(durationMillis = 50, delayMillis = 50)
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(durationMillis = 50)
                        )
                    },
                    contentAlignment = Alignment.Center
                ) { state ->
                    if (state) {
                        AnimatedNavigationBar(
                            modifier = Modifier.height(120.dp),
                            buttons = navigationDestination,
                            enabled = !isLogin,
                            selectedItem = selectedDestination,
                            onClick = { index, destination ->
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
                    }else{
                        Box(modifier = Modifier.fillMaxWidth().height(0.dp))
                    }
                }
                AnimatedContent(
                    modifier = Modifier.align(Alignment.CenterStart).pointerInput(Unit){},
                    targetState = isWideScreen && !isLogin,
                    transitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(durationMillis = 50, delayMillis = 50)
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(durationMillis = 50)
                        )
                    },
                    contentAlignment = Alignment.Center
                ) { state ->
                    if (state) {
                        AnimatedNavigationRail(
                            modifier = Modifier.width(120.dp),
                            enabled = !isLogin,
                            buttons = navigationDestination,
                            selectedItem = selectedDestination,
                            onClick = { index, destination ->
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
                    } else{
                        Box(modifier = Modifier.fillMaxHeight().width(0.dp))
                    }
                }
            }
            if(userState.role == UserRole.ADMIN){
                val offsetY by animateDpAsState(
                    targetValue = if(isWideScreen) (-64).dp else (-100).dp
                )
                if(currentDestination == Screen.Products.route || currentDestination == Screen.Users.route){
                    IconButton(
                        onClick = {
                            if(currentDestination == Screen.Products.route) {
                                addProductModalState = true
                            } else if(currentDestination == Screen.Users.route){
                                addUserModalState = true
                            }
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = ExtendedTheme.colorScheme.quinary.colorContainer
                        ),
                        modifier = Modifier
                            .offset{ IntOffset(x = (-32).dp.toPx().toInt(), y = offsetY.toPx().toInt()) }
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .shadow(elevation = 4.dp, shape = CircleShape)
                            .size(60.dp)
                            .align(Alignment.BottomEnd)
                    ){
                        AnimatedContent(
                            targetState = currentDestination
                        ){ state ->
                            if(state == Screen.Products.route){
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Product Icon"
                                )
                            }else if(state == Screen.Users.route){
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Add User Icon"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


