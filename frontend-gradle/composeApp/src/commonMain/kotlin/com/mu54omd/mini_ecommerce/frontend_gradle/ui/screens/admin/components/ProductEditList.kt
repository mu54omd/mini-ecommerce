package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import coil3.compose.AsyncImage

@Composable
fun ProductEditList(
    products: List<Product>,
    onEditClick: (Long) -> Unit,
    onRemoveClick: (Long) -> Unit,
    modifier: Modifier = Modifier
    ) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(200.dp)
    ) {
        items(items = products, key = { product -> product.id }) { product ->
            Card(
                modifier = Modifier.size(300.dp).padding(8.dp),
                onClick = { },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(0.7f)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.description,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)).height(70.dp)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, overflow = TextOverflow.Ellipsis, modifier = Modifier.basicMarquee())
                            Text("${product.price} $", style = MaterialTheme.typography.bodyMedium)
                            Text("#${product.stock}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        IconButton(
                            onClick = {
                                onRemoveClick(product.id)
                            },
                            enabled = true
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Product"
                            )
                        }
                        IconButton(
                            onClick = {
                                onEditClick
                            },
                            enabled = true
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Product"
                            )
                        }

                    }
                }
            }
        }
    }
}