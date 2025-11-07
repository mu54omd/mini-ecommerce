package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.AddProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.DeleteProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.EditProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.SearchProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.UploadProductImageUseCase

data class ProductUseCases(
    val addProductUseCase: AddProductUseCase,
    val deleteProductUseCase: DeleteProductUseCase,
    val editProductUseCase: EditProductUseCase,
    val getProductsUseCase: GetProductsUseCase,
    val searchProductsUseCase: SearchProductsUseCase,
    val uploadProductImageUseCase: UploadProductImageUseCase
)