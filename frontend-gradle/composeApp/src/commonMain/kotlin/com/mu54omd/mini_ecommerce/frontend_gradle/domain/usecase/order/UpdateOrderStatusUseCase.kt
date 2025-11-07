package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.OrderRepository

class UpdateOrderStatusUseCase(
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke(orderId: Long, status: String): ApiResult<OrderResponse>{
        val user = authRepository.getUserInfo()
        if(!permissionChecker.canManageOrder(user)) return ApiResult.Unauthorized()
        return orderRepository.updateOrderStatus(orderId = orderId, status = status)
    }
}