package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.CartList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.CheckoutDialog
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onExit: (UiState<*>) -> Unit,
    onConfirmClick: () -> Unit
) {
    val cartState = cartViewModel.cartState.collectAsState().value
    var checkoutDialogState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(cartState){
            is UiState.Idle -> {}
            is UiState.Loading -> LoadingView()
            is UiState.Success<CartResponse> -> {
                if (cartState.data.items.isEmpty()) {
                    EmptyPage(title = "Oops!", message = "Your cart is empty")
                } else {
                    CartList(
                        cartItems = cartState.data.items,
                        onCheckoutClick = { checkoutDialogState = true },
                        onClearCartClick = { cartViewModel.clear() }
                    )
                }
                AnimatedVisibility(
                    visible = checkoutDialogState
                ) {
                    CheckoutDialog(
                        cartItems = cartState.data.items,
                        onCancelClick = {
                            checkoutDialogState = false
                        },
                        onConfirmClick = {
                            checkoutDialogState = false
                            onConfirmClick()
                        },
                    )
                }
            }
            else -> onExit(cartState)
        }
    }
}