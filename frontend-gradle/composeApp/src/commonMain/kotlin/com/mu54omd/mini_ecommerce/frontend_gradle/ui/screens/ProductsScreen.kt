package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

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
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.AddOrEditProduct
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.ProductEditList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.ProductList
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userRole: UserRole,
    onExit: (UiState<*>) -> Unit,
) {
    val productsState = productViewModel.productsState.collectAsState().value
    val addProductState = productViewModel.addProductState.collectAsState().value
    val editProductState = productViewModel.editProductState.collectAsState().value
    var addProductModalState by remember { mutableStateOf(false) }
    var editProductModalState by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product>(Product(name = "", description = "", price = 0.0, stock = 0))}

    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItems: Map<Long, Int> = if (cartState is UiState.Success) cartState.data.items.associate { (_, product, quantity) -> product.id to quantity } else emptyMap()


    val scope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single
    ) { file ->
        file?.let {
            scope.launch {
                if(addProductState is UiState.Success) {
                    productViewModel.uploadProductImage(
                        productId = addProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                    productViewModel.resetAddProductState()
                }
                if(editProductState is UiState.Success) {
                    productViewModel.uploadProductImage(
                        productId = editProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                    productViewModel.resetEditProductState()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
        cartViewModel.getCart()
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
                    if (userRole == UserRole.ADMIN) {
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
                                productViewModel.deleteProduct(productId)
                            },
                            modifier = Modifier.weight(0.9f)
                        )
                        if (addProductModalState) {
                            AddOrEditProduct(
                                productState = addProductState,
                                onCancelClick = {
                                    addProductModalState = false
                                    productViewModel.resetAddProductState()
                                },
                                onConfirmClick = { name, description, price, stocks ->
                                    productViewModel.addProduct(
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
                        if (editProductModalState) {
                            AddOrEditProduct(
                                product = selectedProduct,
                                productState = editProductState,
                                onCancelClick = {
                                    editProductModalState = false
                                    productViewModel.resetEditProductState()
                                },
                                onConfirmClick = { name, description, price, stocks ->
                                    productViewModel.editProduct(
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
                    }else{
                        ProductList(
                            products = productsState.data,
                            cartItems = cartItems,
                            onAddClick = { cartViewModel.add(it) },
                            onRemoveClick = { cartViewModel.remove(it) },
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                }
            }
        }

        else -> onExit(productsState)
    }
}
