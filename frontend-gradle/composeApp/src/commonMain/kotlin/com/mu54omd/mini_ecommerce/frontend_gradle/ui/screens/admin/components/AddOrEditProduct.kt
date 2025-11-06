package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
fun AddOrEditProduct(
    productState: UiState<Product>,
    product: Product? = null,
    onCancelClick: () -> Unit,
    onConfirmClick: (String, String, Double, Int) -> Unit,
) {
    var productName by remember { mutableStateOf(product?.name ?: "") }
    var productDescription by remember { mutableStateOf(product?.description ?: "") }
    var productPrice by remember { mutableStateOf(if (product?.price == null) "" else product.price.toString()) }
    var productStocks by remember { mutableStateOf(if (product?.stock == null) "" else product.stock.toString()) }
    var currentStep by remember { mutableStateOf(ProductModalStep.FORM) }

    LaunchedEffect(productState) {
        if (productState is UiState.Success) {
            currentStep = ProductModalStep.IMAGES
        }
    }

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = rememberModalBottomSheetState(),
        ) {
        when (currentStep) {
            ProductModalStep.FORM -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = {
                            Text(
                                text = "Product Name",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = {
                            Text(
                                text = "Product Description",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
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
                                text = "Product Price",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
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
                                text = "Product Stocks",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { onCancelClick() }) {
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
                            enabled = productName.isNotBlank() && productPrice.isNotBlank() && productDescription.isNotBlank() && productStocks.isNotBlank()
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
                    .padding(10.dp)
                ){
                    Text("Upload Image")
                }
            }
        }
    }
}
