package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.GetOrdersUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.GetUserOrderUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.UpdateOrderStatusUseCase

data class OrderUseCases(
    val getOrdersUseCase: GetOrdersUseCase,
    val getUserOrderUseCase: GetUserOrderUseCase,
    val updateOrderStatusUseCase: UpdateOrderStatusUseCase
)
