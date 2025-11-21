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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme

@Composable
fun SearchBar(
    placeHolderText: String = "Search",
    onQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchBarText by remember { mutableStateOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val errorColor = MaterialTheme.colorScheme.error
    val inversePrimaryColor = MaterialTheme.colorScheme.inversePrimary
    val quaternaryColor = ExtendedTheme.colorScheme.quaternary.color
    val quinaryColor = ExtendedTheme.colorScheme.quinary.color

    val lineBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                primaryColor,
                secondaryColor,
                tertiaryColor,
                errorColor,
                inversePrimaryColor,
                quinaryColor,
                quaternaryColor
            ).shuffled()
        )
    }
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
                IconButton(
                    onClick = {
                        searchBarText = ""
                        onClearQuery()
                    },
                    modifier = Modifier.size(20.dp)
                ) {
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
                text = placeHolderText,
                style = MaterialTheme.typography.bodySmall.copy(brush = lineBrush),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                overflow = TextOverflow.Ellipsis
            )
        },
        textStyle = MaterialTheme.typography.bodySmall.copy(brush = lineBrush)
    )
}