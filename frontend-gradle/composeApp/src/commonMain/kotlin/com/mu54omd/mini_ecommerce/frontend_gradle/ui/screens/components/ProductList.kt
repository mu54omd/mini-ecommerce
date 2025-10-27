package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product

@Composable
fun ProductList(
    products: List<Product>,
    cartItems: Map<Long,Int>,
    onAddClick: (Long) -> Unit,
    onRemoveClick: (Long) -> Unit,
    modifier: Modifier = Modifier
    ) {

    LazyColumn(
        modifier = modifier
    ) {
        items(items = products, key = { product -> product.id}) { product ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                onClick = {  },
            ) {
                var addedItem by rememberSaveable { mutableIntStateOf(cartItems[product.id]?: 0) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ){
                        Text(product.name, fontWeight = FontWeight.Bold)
                        Text("${product.price} $")
                        Text("#${product.stock}")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        IconButton(
                            onClick = {
                                addedItem--
                                onRemoveClick(product.id)
                            },
                            enabled = addedItem > 0
                        ){
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Remove Product from Cart"
                            )
                        }
                        Text(text = "$addedItem")
                        IconButton(
                            onClick = {
                                addedItem++
                                onAddClick(product.id)
                                      },
                            enabled = (addedItem < product.stock) &&  (product.stock > 0)
                        ){
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Product to Cart"
                            )
                        }

                    }
                }
            }
        }
    }
}