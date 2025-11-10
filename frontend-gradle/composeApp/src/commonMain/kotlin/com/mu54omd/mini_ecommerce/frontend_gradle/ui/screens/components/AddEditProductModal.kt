package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import kotlinx.coroutines.launch


enum class ProductModalStep {
    FORM,
    IMAGES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductModal(
    productState: UiState<Product>,
    product: Product? = null,
    onCancelClick: () -> Unit,
    onConfirmClick: (String, String, Double, Int) -> Unit,
    onUploadImageClick: () -> Unit,
) {
    var productName by rememberSaveable { mutableStateOf(product?.name ?: "") }
    var productDescription by rememberSaveable { mutableStateOf(product?.description ?: "") }
    var productPrice by rememberSaveable { mutableStateOf(if (product?.price == null) "" else product.price.toString()) }
    var productStocks by rememberSaveable { mutableStateOf(if (product?.stock == null) "" else product.stock.toString()) }
    var currentStep by rememberSaveable { mutableStateOf(ProductModalStep.FORM) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(productState) {
        if (productState is UiState.Success) {
            currentStep = ProductModalStep.IMAGES
        }
    }

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = .5f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            when (currentStep) {
                ProductModalStep.FORM -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .statusBarsPadding()
                            .width(350.dp)
                            .height(400.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceBright,
                                shape = RoundedCornerShape(5)
                            )
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            label = {
                                Text(
                                    text = "Name",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.width(300.dp)
                        )
                        OutlinedTextField(
                            value = productDescription,
                            onValueChange = { productDescription = it },
                            label = {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            modifier = Modifier.height(150.dp).width(300.dp),
                            maxLines = 5,
                            singleLine = false,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.width(300.dp)
                        ) {
                            OutlinedTextField(
                                value = productPrice,
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() || it == '.' }) {
                                        productPrice = newValue
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                label = {
                                    Text(
                                        text = "Price",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.width(170.dp)
                            )

                            OutlinedTextField(
                                value = productStocks,
                                onValueChange = { newValue ->
                                    if (newValue.all(Char::isDigit)) {
                                        productStocks = newValue
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                label = {
                                    Text(
                                        text = "Stocks",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.width(120.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.width(300.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onCancelClick()
                                    }
                                },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Text(text = "Cancel")
                            }
                            TextButton(
                                onClick = {
                                    onConfirmClick(
                                        productName,
                                        productDescription,
                                        productPrice.toDouble(),
                                        productStocks.toInt()
                                    )
                                },
                                enabled = productName.isNotBlank() && productPrice.isNotBlank() && productDescription.isNotBlank() && productStocks.isNotBlank(),
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

                            ) {
                                Text(text = "Save")
                            }
                        }
                    }
                }

                ProductModalStep.IMAGES -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .statusBarsPadding()
                            .width(350.dp)
                            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(5))
                            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp)
                    ) {
                        Text(
                            text = "Do you want to upload an image for this product?",
                            textAlign = TextAlign.Center
                        )
                        Row {
                            TextButton(
                                onClick = {
                                    onCancelClick()
                                },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Text("Cancel")
                            }
                            TextButton(
                                onClick = {
                                    onUploadImageClick()
                                },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Text("Upload Image")
                            }
                        }
                    }
                }
            }
        }
    }
}
