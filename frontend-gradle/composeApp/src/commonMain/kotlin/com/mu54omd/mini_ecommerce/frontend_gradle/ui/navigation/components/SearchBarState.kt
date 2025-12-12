package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components

data class SearchBarState(
    val isVisible: Boolean = false,
    val placeHolderText: String = "",
    val onSearchQuery: (String) -> Unit = {},
    val onClearSearchQuery: () -> Unit = {},
)