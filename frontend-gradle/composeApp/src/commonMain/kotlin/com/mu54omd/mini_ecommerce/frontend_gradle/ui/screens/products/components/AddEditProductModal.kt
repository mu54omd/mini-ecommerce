package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.AppThemeExtras
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductModal(
    productImage: PlatformFile? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    product: Product,
    onCancelClick: () -> Unit,
    onConfirmClick: (String, String, String , Double, Int) -> Unit,
    onUploadImageClick: (String, String, String, Double, Int) -> Unit,
) {

    var productName by rememberSaveable { mutableStateOf(product.name) }
    var productCategory by rememberSaveable { mutableStateOf(product.category) }
    var productDescription by rememberSaveable { mutableStateOf(product.description) }
    var productPrice by rememberSaveable { mutableStateOf(product.price.toString()) }
    var productStocks by rememberSaveable { mutableStateOf(product.stock.toString()) }


    val lineBrush = AppThemeExtras.brushes.lineBrush

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.5f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ){
                    onCancelClick()
                }
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .statusBarsPadding()
                    .width(350.dp)
                    .height(500.dp)
                    .border(width = 2.dp, brush = lineBrush, shape = RoundedCornerShape(5))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = RoundedCornerShape(5)
                    )
                    .padding(8.dp)
                    .clickable(
                        enabled = false,
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ){}
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(300.dp)
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
                        modifier = Modifier.width(160.dp),
                        shape = RoundedCornerShape(30),
                        textStyle = TextStyle(brush = lineBrush)
                    )
                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { productCategory = it },
                        label = {
                            Text(
                                text = "Category",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.width(135.dp),
                        shape = RoundedCornerShape(30),
                        textStyle = TextStyle(brush = lineBrush)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
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
                    shape = RoundedCornerShape(10),
                    maxLines = 5,
                    singleLine = false,
                    textStyle = TextStyle(brush = lineBrush)
                )
                Spacer(modifier = Modifier.height(4.dp))
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
                        shape = RoundedCornerShape(30),
                        modifier = Modifier.width(170.dp),
                        textStyle = TextStyle(brush = lineBrush)
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
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(30),
                        textStyle = TextStyle(brush = lineBrush)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = productImage?.name ?: "",
                    onValueChange = { },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(300.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onUploadImageClick(
                                    productName,
                                    productCategory,
                                    productDescription,
                                    productPrice.toDouble(),
                                    productStocks.toInt()
                                )
                            },
                            modifier = Modifier.size(50.dp)
                                .pointerHoverIcon(icon = PointerIcon.Hand)
                        ){
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Upload Image",
                            )
                        }
                    },
                    shape = RoundedCornerShape(30),
                    textStyle = TextStyle(brush = lineBrush)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.width(300.dp)
                ) {
                    TextButton(
                        onClick = { onCancelClick() },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel Edit"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirmClick(
                                productName,
                                productCategory,
                                productDescription,
                                productPrice.toDouble(),
                                productStocks.toInt()
                            )
                        },
                        enabled = productName.isNotBlank() && productCategory.isNotBlank() && productPrice.isNotBlank() && productDescription.isNotBlank() && productStocks.isNotBlank(),
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Confirm Edit"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}
