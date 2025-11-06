package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import coil3.compose.AsyncImage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.Constants.BASE_URL

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
        items(items = products, key = { product -> product.id!! }) { product ->
            Card(
                modifier = Modifier.size(300.dp).padding(8.dp),
                onClick = { },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(0.7f)
                ) {
                    product.imageUrl?.let {
                        AsyncImage(
                            model = "$BASE_URL${product.imageUrl}",
                            contentDescription = product.description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Image(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "Product With No Image",
                        modifier = Modifier.fillMaxSize(0.5f),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                    alpha = 0.7f
                                )
                            ).height(70.dp)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                product.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.basicMarquee(),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "${product.price} $",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "#${product.stock}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        IconButton(
                            onClick = {
                                onRemoveClick(product.id!!)
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