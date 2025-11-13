package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.AddEditProductModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.DeleteModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.ProductList
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.error_alert
import frontend_gradle.composeapp.generated.resources.upload_image_successful_alert
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductsScreen(
    isWideScreen: Boolean = false,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userRole: UserRole,
    onExit: (UiState<*>) -> Unit,
) {
    val productsState = productViewModel.productsState.collectAsState().value
    val addProductState = productViewModel.addProductState.collectAsState().value
    val editProductState = productViewModel.editProductState.collectAsState().value
    val deactivateProduct = productViewModel.deactivateProduct.collectAsState().value
    val uploadProductImageState = productViewModel.uploadProductImageState.collectAsState().value

    var addProductModalState by remember { mutableStateOf(false) }
    var editProductModalState by remember { mutableStateOf(false) }
    var deleteProductModalState by remember { mutableStateOf(false) }
    var showAlertModalState by remember { mutableStateOf(false) }

    var selectedProduct by remember {
        mutableStateOf(
            Product(
                name = "",
                description = "",
                price = 0.0,
                stock = 0
            )
        )
    }

    var selectedProductIdForDelete by remember { mutableLongStateOf(-1) }

    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItems: Map<Long, Int> =
        if (cartState is UiState.Success) cartState.data.items.associate { (_, product, quantity) -> product.id to quantity } else emptyMap()


    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single
    ) { file ->
        file?.let {
            scope.launch {
                if (addProductState is UiState.Success) {
                    productViewModel.uploadProductImage(
                        productId = addProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                }
                if (editProductState is UiState.Success) {
                    productViewModel.uploadProductImage(
                        productId = editProductState.data.id!!,
                        fileName = file.name,
                        byteArray = file.readBytes()
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        productViewModel.refreshProducts()
        cartViewModel.getCart()
    }

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = lazyGridState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= total - 4) {
                    productViewModel.loadNextPage()
                }
            }
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
                        TextButton(
                            onClick = {
                                addProductModalState = true
                            },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text("Add Product")
                        }
                    }
                    ProductList(
                        isWideScreen = isWideScreen,
                        lazyGridState = lazyGridState,
                        userRole = userRole,
                        products = productsState.data,
                        cartItems = cartItems,
                        onIncreaseItem = { cartViewModel.add(it) },
                        onDecreaseItem = { cartViewModel.remove(it) },
                        onEditClick = { product ->
                            selectedProduct = product
                            editProductModalState = true
                        },
                        onRemoveClick = { productId ->
                            selectedProductIdForDelete = productId
                            deleteProductModalState = true
                        },
                        modifier = Modifier.weight(0.9f)
                    )
                    if (addProductModalState) {
                        AddEditProductModal(
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
                        AddEditProductModal(
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
                    if (deleteProductModalState) {
                        DeleteModal(
                            id = selectedProductIdForDelete,
                            onCancelClick = {
                                deleteProductModalState = false
                                selectedProductIdForDelete = -1
                            },
                            onConfirmClick = { productId ->
                                deleteProductModalState = false
                                productViewModel.deactivateProduct(productId)
                                selectedProductIdForDelete = -1
                            }
                        )
                    }
                    when (addProductState) {
                        is UiState.Idle -> {}
                        is UiState.Loading -> {}
                        is UiState.Success -> {}
                        else -> {
                            showAlertModalState = true
                            if (showAlertModalState) {
                                AlertModal(
                                    message = stringResource(Res.string.error_alert),
                                    onConfirmClick = {
                                        showAlertModalState = false
                                        productViewModel.resetAddProductState()
                                    }
                                )
                            }
                        }
                    }
                    when (editProductState) {
                        is UiState.Idle -> {}
                        is UiState.Loading -> {}
                        is UiState.Success -> {}
                        else -> {
                            showAlertModalState = true
                            if (showAlertModalState) {
                                AlertModal(
                                    message = stringResource(Res.string.error_alert),
                                    onConfirmClick = {
                                        showAlertModalState = false
                                        productViewModel.resetEditProductState()
                                    }
                                )
                            }
                        }
                    }
                    when (deactivateProduct) {
                        is UiState.Idle -> {}
                        is UiState.Loading -> {}
                        is UiState.Success -> {}
                        else -> {
                            println(deactivateProduct.toString())
                            showAlertModalState = true
                            if (showAlertModalState) {
                                AlertModal(
                                    message = stringResource(Res.string.error_alert),
                                    onConfirmClick = {
                                        showAlertModalState = false
                                        productViewModel.resetDeleteProductState()
                                    }
                                )
                            }
                        }
                    }
                    when (uploadProductImageState) {
                        is UiState.Idle -> {}
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showAlertModalState = true
                            if (showAlertModalState) {
                                AlertModal(
                                    message = stringResource(Res.string.upload_image_successful_alert),
                                    onConfirmClick = {
                                        showAlertModalState = false
                                        productViewModel.resetAddProductState()
                                        productViewModel.resetEditProductState()
                                        productViewModel.resetUploadProductImageState()
                                    }
                                )
                            }
                        }

                        else -> {
                            showAlertModalState = true
                            if (showAlertModalState) {
                                AlertModal(
                                    message = stringResource(Res.string.error_alert),
                                    onConfirmClick = {
                                        showAlertModalState = false
                                        productViewModel.resetUploadProductImageState()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        else -> onExit(productsState)
    }
}
