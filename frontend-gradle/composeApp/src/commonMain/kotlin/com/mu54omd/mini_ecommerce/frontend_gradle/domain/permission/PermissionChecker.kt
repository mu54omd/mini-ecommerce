package com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole

class PermissionChecker {
    fun canEditProduct(user: User): Boolean = user.role == UserRole.ADMIN

    fun canManageUser(user: User): Boolean = user.role == UserRole.ADMIN

    fun canManageOrder(user: User): Boolean = user.role == UserRole.ADMIN

    fun canViewOrder(user: User): Boolean = user.role == UserRole.USER || user.role == UserRole.ADMIN

    fun canPlaceOrder(user: User): Boolean = user.role == UserRole.ADMIN || user.role == UserRole.USER
}