package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    onQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchBarText by remember { mutableStateOf("") }
    OutlinedTextField(
        onValueChange = {
            searchBarText = it
            if (it.isBlank()) {
                onClearQuery()
            } else {
                onQuery(it)
            }
        },
        value = searchBarText,
        shape = RoundedCornerShape(50),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Bar Icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (searchBarText.isNotBlank())
                IconButton(onClick = {
                    searchBarText = ""
                    onClearQuery()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search Bar Icon",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                }
        },
        modifier = modifier,
        maxLines = 1,
        placeholder = {
            Text(
                "Search Products",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}