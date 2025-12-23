package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
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
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.DeleteModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components.AddEditProductModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components.ProductList
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    isWideScreen: Boolean = false,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userRole: UserRole,
    addProductModalState: Boolean,
    onAddProductModalChange: (Boolean) -> Unit,
    onExit: (String?) -> Unit
) {
    val state by productViewModel.state.collectAsState()
    val effect = productViewModel.effect
    val cartState by cartViewModel.state.collectAsState()

    var editProductModalState by remember { mutableStateOf(false) }
    var deleteProductModalState by remember { mutableStateOf(false) }

    var alertMessage by remember { mutableStateOf<String?>(null) }

    var selectedProduct by remember { mutableStateOf(Product()) }
    var tempImageFile by remember { mutableStateOf<PlatformFile?>(null) }
    var selectedProductIdForDelete by remember { mutableLongStateOf(-1) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lazyGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    // ======================== File Pickers ========================
    val addProductLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        title = "Add Product Image"
    ) { file ->
        onAddProductModalChange(true)
        file?.let { tempImageFile = it }
    }

    val editProductLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        title = "Edit Product Image"
    ) { file ->
        editProductModalState = true
        file?.let { tempImageFile = it }
    }

    // ======================== Initial Load ========================
    LaunchedEffect(Unit) {
        productViewModel.refresh()
        if(userRole == UserRole.USER || userRole == UserRole.ADMIN) {
            cartViewModel.loadCart()
        }
    }

    // ======================== Pagination Trigger ========================
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisible ->
                val total = lazyGridState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= total - 4) {
                    productViewModel.loadNextPage()
                }
            }
    }

    // ======================== Alert ========================
    LaunchedEffect(Unit) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is ProductUiEffect.ShowMessage -> { alertMessage = uiEffect.message }
                is ProductUiEffect.ShowError -> { alertMessage = uiEffect.message }
                is ProductUiEffect.NavigateToLogin -> { onExit(uiEffect.message) }
            }
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 16.dp)
    ) {
        // ======================== Product List ========================
        ProductList(
            isWideScreen = isWideScreen,
            lazyGridState = lazyGridState,
            isInitialLoading = state.isInitialLoading,
            isRefreshing = state.isRefreshing,
            userRole = userRole,
            latestProductsBanner = state.banner,
            products = state.products,
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onSelectCategory = productViewModel::onCategorySelected,
            cartItems = cartState.itemsCount,
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
            modifier = Modifier
        )
        if(state.isInitialLoading) {
                LoadingView()
        }
        // ======================== Add Product Modal ========================
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
                        ),
                        tempImageFile
                    )
                    onAddProductModalChange(false)
                    tempImageFile = null
                },
                onUploadImageClick = { name, category, description, price, stocks ->
                    scope.launch {
                        sheetState.hide()
                        selectedProduct = Product(
                            name = name,
                            category = category,
                            description = description,
                            price = price,
                            stock = stocks
                        )
                        onAddProductModalChange(false)
                    }
                    addProductLauncher.launch()
                }
            )
        }

        // ======================== Edit Product Modal ========================
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
                        selectedProduct = Product()
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
                        ),
                        tempImageFile
                    )
                    editProductModalState = false
                    tempImageFile = null
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

        // ======================== Delete Product Modal ========================
        if (deleteProductModalState) {
            DeleteModal(
                id = selectedProductIdForDelete,
                onCancelClick = {
                    deleteProductModalState = false
                    selectedProductIdForDelete = -1
                },
                onConfirmClick = { productId ->
                    productViewModel.deactivateProduct(productId)
                    deleteProductModalState = false
                    selectedProductIdForDelete = -1
                }
            )
        }
        alertMessage?.let {
            AlertModal(
                message = it,
                onConfirmClick = {
                    alertMessage = null
                }
            )
        }
    }
}
