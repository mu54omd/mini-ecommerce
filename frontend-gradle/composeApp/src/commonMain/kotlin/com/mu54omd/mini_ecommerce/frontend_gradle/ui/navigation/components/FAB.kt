package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme

@Composable
fun BoxScope.FAB(
    userRole: UserRole,
    isWideScreen: Boolean,
    currentDestination: String?,
    onFabClick: () -> Unit,
){
    if(userRole == UserRole.ADMIN){
        val offsetY by animateDpAsState(
            targetValue = if(isWideScreen) (-64).dp else (-100).dp
        )
        if(currentDestination == Screen.Products.route || currentDestination == Screen.Users.route){
            IconButton(
                onClick = onFabClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = ExtendedTheme.colorScheme.quinary.colorContainer
                ),
                modifier = Modifier
                    .offset{ IntOffset(x = (-32).dp.toPx().toInt(), y = offsetY.toPx().toInt()) }
                    .pointerHoverIcon(icon = PointerIcon.Hand)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            ){
                AnimatedContent(
                    targetState = currentDestination
                ){ state ->
                    if(state == Screen.Products.route){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Product Icon"
                        )
                    }else if(state == Screen.Users.route){
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add User Icon"
                        )
                    }
                }
            }
        }
    }
}