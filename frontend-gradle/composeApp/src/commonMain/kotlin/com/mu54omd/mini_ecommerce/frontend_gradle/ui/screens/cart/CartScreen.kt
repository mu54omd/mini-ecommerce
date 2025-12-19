package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.components.CartList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.components.CheckoutModal

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
) {

    val state by cartViewModel.state.collectAsState()
    var alertMessage by remember { mutableStateOf<String?>(null) }
    var showCheckoutModal by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
    }

    LaunchedEffect(Unit) {
        cartViewModel.effect.collect { effect ->
            alertMessage = when (effect) {
                is CartUiEffect.ShowError -> effect.message
                is CartUiEffect.CheckoutSuccess -> effect.message + effect.id
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.isEmpty && !state.isInitialLoading && !state.isRefreshing) {
            EmptyPage("Oops!", "Your cart is empty!")

        } else {
            CartList(
                cartItems = state.items,
                onCheckoutClick = { showCheckoutModal = true },
                onClearCartClick = cartViewModel::clear
            )
        }
        if (state.isInitialLoading) {
            LoadingView()
        }

        if (showCheckoutModal) {
            CheckoutModal(
                cartItems = state.items,
                onCancelClick = { showCheckoutModal = false },
                onConfirmClick = {
                    showCheckoutModal = false
                    cartViewModel.checkout()
                }
            )
        }
        alertMessage?.let {
            AlertModal(
                message = it,
                onConfirmClick = { alertMessage = null }
            )
        }
    }
}