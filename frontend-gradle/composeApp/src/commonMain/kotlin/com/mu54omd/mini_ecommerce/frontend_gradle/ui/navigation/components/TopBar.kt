package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.MainMenu
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.SearchBar

@Composable
fun TopBar(
    isLogin: Boolean,
    isMainMenuHidden: Boolean,
    isDarkTheme: Boolean,
    searchBarState: SearchBarState,
    onMainMenuClick: () -> Unit,
    onMainMenuDismiss: () -> Unit,
    onToggleTheme: () -> Unit,
    onLogoutClick: () -> Unit,

){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .height(64.dp)
            .graphicsLayer {
                alpha = if (isLogin) 0f else 1f
            },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default).align(Alignment.CenterStart),
            enabled = !isLogin
        ){
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Drawer Menu Icon",
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            if(searchBarState.isVisible){
                SearchBar(
                    placeHolderText = searchBarState.placeHolderText,
                    onQuery = searchBarState.onSearchQuery,
                    onClearQuery = searchBarState.onClearSearchQuery
                )
            }
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = onMainMenuClick,
                    modifier = Modifier.pointerHoverIcon(if (!isLogin) PointerIcon.Hand else PointerIcon.Default),
                    enabled = !isLogin
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Main Menu Icon"
                    )
                }
                MainMenu(
                    isExpanded = !isMainMenuHidden,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onLogoutClick = onLogoutClick,
                    onDismiss = onMainMenuDismiss
                )
            }
        }
    }
}