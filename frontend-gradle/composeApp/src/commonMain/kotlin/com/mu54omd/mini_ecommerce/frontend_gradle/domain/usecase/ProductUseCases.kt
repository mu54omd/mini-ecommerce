package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.AddProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.DeactivateProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.DeleteProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.EditProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetCategoriesUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetLatestProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetProductsByCategoryUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.SearchProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.UploadProductImageUseCase

data class ProductUseCases(
    val addProductUseCase: AddProductUseCase,
    val deleteProductUseCase: DeleteProductUseCase,
    val deactivateProductUseCase: DeactivateProductUseCase,
    val editProductUseCase: EditProductUseCase,
    val getProductsUseCase: GetProductsUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase,
    val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    val getLatestProductsUseCase: GetLatestProductsUseCase,
    val searchProductsUseCase: SearchProductsUseCase,
    val uploadProductImageUseCase: UploadProductImageUseCase
)