package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun ProductSearchBar(
    onQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
){
    var searchBarText by remember { mutableStateOf("") }
    OutlinedTextField(
        onValueChange = {
            searchBarText = it
            if(it.isBlank()) {
                onClearQuery()
            }else{
                onQuery(it)
            }
        },
        value = searchBarText,
        shape = RoundedCornerShape(50),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Bar Icon") },
        trailingIcon = {
            if(searchBarText.isNotBlank())
                IconButton( onClick = {
                    searchBarText = ""
                    onClearQuery()
                } ){
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search Bar Icon")
                }
        },
        modifier = Modifier.scale(0.8f),
        maxLines = 1,
    )
}