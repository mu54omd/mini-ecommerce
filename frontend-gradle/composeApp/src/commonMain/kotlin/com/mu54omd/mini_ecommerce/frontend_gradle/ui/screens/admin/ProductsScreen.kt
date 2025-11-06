package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AdminViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components.AddOrEditProduct
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components.ProductEditList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(
    adminViewModel: AdminViewModel,
    onExit: (UiState<*>) -> Unit,
) {
    val productsState = adminViewModel.productsState.collectAsState().value
    val addProductState = adminViewModel.addProductState.collectAsState().value
    val editProductState = adminViewModel.editProductState.collectAsState().value
    var addProductModalState by remember { mutableStateOf(false) }
    var editProductModalState by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product>(Product(name = "", description = "", price = 0.0, stock = 0))}
    val scope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single
    ) { file ->
        file?.let {
            scope.launch {
                if(addProductState is UiState.Success) {
                    adminViewModel.uploadProductImage(
                        productId = addProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                    adminViewModel.resetAddProductState()
                }
                if(editProductState is UiState.Success) {
                    adminViewModel.uploadProductImage(
                        productId = editProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                    adminViewModel.resetEditProductState()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        adminViewModel.refresh()
    }

    when (productsState) {
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            if (productsState.data.isEmpty()) {
                EmptyPage("Oops!", "No product found!")
            } else {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    TextButton(onClick = {
                        addProductModalState = true
                    }) {
                        Text("Add Product")
                    }
                    ProductEditList(
                        products = productsState.data,
                        onEditClick = { product ->
                            selectedProduct = product
                            editProductModalState = true
                        },
                        onRemoveClick = { productId ->
                            adminViewModel.deleteProduct(productId)
                        },
                        modifier = Modifier.weight(0.9f)
                    )
                }
                if(addProductModalState) {
                    AddOrEditProduct(
                        productState = addProductState,
                        onCancelClick = {
                            addProductModalState = false
                            adminViewModel.resetAddProductState()
                                        },
                        onConfirmClick = { name, description, price, stocks ->
                            adminViewModel.addProduct(
                                Product(
                                    name = name,
                                    description = description,
                                    price = price,
                                    stock = stocks
                                )
                            )
                        },
                        onUploadImageClick = {
                            addProductModalState = false
                            launcher.launch()
                        }
                    )
                }
                if(editProductModalState) {
                    AddOrEditProduct(
                        product = selectedProduct,
                        productState = editProductState,
                        onCancelClick = {
                            editProductModalState = false
                            adminViewModel.resetEditProductState()
                        },
                        onConfirmClick = { name, description, price, stocks ->
                            adminViewModel.editProduct(
                                Product(
                                    id = selectedProduct.id,
                                    name = name,
                                    description = description,
                                    price = price,
                                    stock = stocks
                                )
                            )
                        },
                        onUploadImageClick = {
                            editProductModalState = false
                            launcher.launch()
                        }
                    )
                }
            }
        }

        else -> onExit(productsState)
    }
}
