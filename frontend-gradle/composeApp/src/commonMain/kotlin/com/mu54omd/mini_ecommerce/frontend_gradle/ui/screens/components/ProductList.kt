package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products.size) { index ->
            val product = products[index]
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = androidx.compose.ui.Modifier
                    .padding(8.dp)
            ) {
                Column(
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                ) {
                    Text(product.name)
                    Text(product.description)
                    Text("💰 ${product.price}")
                }
            }
        }
    }
}