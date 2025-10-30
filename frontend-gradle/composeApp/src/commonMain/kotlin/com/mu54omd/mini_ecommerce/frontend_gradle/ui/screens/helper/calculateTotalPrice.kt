package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper

import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartItemResponse

fun List<CartItemResponse>.calculateTotalPrice(): Double {
    return this.fold(0.0) { acc, item -> acc + item.quantity * item.product.price }
}