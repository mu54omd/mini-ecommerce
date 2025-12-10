package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.DeleteModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components.AddEditProductModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components.ProductList
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.error_alert
import frontend_gradle.composeapp.generated.resources.upload_image_successful_alert
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    isWideScreen: Boolean = false,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userRole: UserRole,
    addProductModalState: Boolean,
    onAddProductModalChange: (Boolean) -> Unit,
    onExit: (UiState<*>) -> Unit,
) {
    val productsState = productViewModel.productsState.collectAsState()
    val categoriesState = productViewModel.categoriesState.collectAsState()
    val selectedCategory = productViewModel.selectedCategory.collectAsState()
    val latestProductsBannerState = productViewModel.latestProductsBannerState.collectAsState()
    val addProductState = productViewModel.addProductState.collectAsState().value
    val editProductState = productViewModel.editProductState.collectAsState().value
    val deactivateProduct = productViewModel.deactivateProduct.collectAsState().value
    val uploadProductImageState = productViewModel.uploadProductImageState.collectAsState().value

    var editProductModalState by remember { mutableStateOf(false) }
    var deleteProductModalState by remember { mutableStateOf(false) }
    var showAlertModalState by remember { mutableStateOf(false) }

    var selectedProduct by remember { mutableStateOf(Product()) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedProductIdForDelete by remember { mutableLongStateOf(-1) }

    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItems: Map<Long, Int> = if (cartState is UiState.Success) cartState.data.items.associate { (_, product, quantity) -> product.id to quantity } else emptyMap()


    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()

    var tempImageFile by remember { mutableStateOf<PlatformFile?>(null) }

    val addProductLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        title = "Add Product Image"
    ) { file ->
        onAddProductModalChange(true)
        file?.let {
            tempImageFile = file
        }
    }
    val editProductLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        title = "Edit Product Image"
    ) { file ->
        editProductModalState = true
        file?.let {
            tempImageFile = file
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
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 16.dp)
    ) {
        ProductList(
            isWideScreen = isWideScreen,
            lazyGridState = lazyGridState,
            userRole = userRole,
            latestProductsBannerState = latestProductsBannerState,
            productsState = productsState,
            categoriesState = categoriesState,
            selectedCategory = selectedCategory,
            onSelectCategory = productViewModel::selectCategory,
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
            onExit = onExit,
            modifier = Modifier.weight(0.9f)
        )
        if (addProductModalState) {
            AddEditProductModal(
                productImage = tempImageFile,
                sheetState = sheetState,
                product = selectedProduct,
                onCancelClick = {
                    scope.launch {
                        sheetState.hide()
                        tempImageFile = null
                        selectedProduct = Product()
                        onAddProductModalChange(false)
                        productViewModel.resetAddProductState()
                    }
                },
                onConfirmClick = { name, category, description, price, stocks ->
                    productViewModel.addProduct(
                        Product(
                            name = name,
                            category = category,
                            description = description,
                            price = price,
                            stock = stocks
                        )
                    )
                    onAddProductModalChange(false)
                },
                onUploadImageClick = { name, category, description, price, stocks ->
                    scope.launch {
                        sheetState.hide()
                        onAddProductModalChange(false)
                        selectedProduct = Product(
                            name = name,
                            category = category,
                            description = description,
                            price = price,
                            stock = stocks
                        )
                    }
                    addProductLauncher.launch()
                }
            )
        }
        if (editProductModalState) {
            AddEditProductModal(
                productImage = tempImageFile,
                product = selectedProduct,
                sheetState = sheetState,
                onCancelClick = {
                    scope.launch {
                        sheetState.hide()
                        editProductModalState = false
                        tempImageFile = null
                        selectedProduct = Product(
                            name = "",
                            description = "",
                            price = 0.0,
                            stock = 0
                        )
                        productViewModel.resetEditProductState()
                    }
                },
                onConfirmClick = { name, category, description, price, stocks ->
                    productViewModel.editProduct(
                        Product(
                            id = selectedProduct.id,
                            name = name,
                            category = category,
                            description = description,
                            price = price,
                            stock = stocks
                        )
                    )
                    editProductModalState = false
                },
                onUploadImageClick = { _, _, _, _, _ ->
                    scope.launch {
                        sheetState.hide()
                        editProductModalState = false
                    }
                    editProductLauncher.launch()
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
            is UiState.Success -> {
                scope.launch {
                    tempImageFile?.let {
                        productViewModel.uploadProductImage(
                            productId = addProductState.data.id!!,
                            fileName = it.name,
                            byteArray = it.readBytes()
                        )
                    }
                }
            }

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
            is UiState.Success -> {
                scope.launch {
                    tempImageFile?.let {
                        productViewModel.uploadProductImage(
                            productId = editProductState.data.id!!,
                            fileName = it.name,
                            byteArray = it.readBytes()
                        )
                    }
                }
            }

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
