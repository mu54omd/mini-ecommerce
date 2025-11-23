package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.SwipeableCard
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserCard(
    user: UserResponse,
    isExpanded: Boolean,
    onCardClick: () -> Unit,
    onEditUserClick: () -> Unit,
    onDeleteUserClick: () -> Unit,
) {
    val adminColor = MaterialTheme.colorScheme.errorContainer
    val userColor = MaterialTheme.colorScheme.tertiaryContainer
    val shapeColor by remember { mutableStateOf(if (user.role == "ADMIN") adminColor else userColor) }
    SwipeableCard(
        maxSwipe = 100f,
        onCardClick = onCardClick,
        content = {
            UserCardContent(
                user = user,
                isExpanded = isExpanded,
                shapeColor = shapeColor,
            )
        },
        actions = {
            BackgroundContent(
                onEditUserClick = onEditUserClick,
                onDeleteUserClick = onDeleteUserClick
            )
        }
    )
}

@Composable
fun BackgroundContent(
    onEditUserClick: () -> Unit,
    onDeleteUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(
            onClick = onEditUserClick,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(
            onClick = onDeleteUserClick,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun UserCardContent(
    user: UserResponse,
    isExpanded: Boolean,
    shapeColor: Color,
){
    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 52.dp
    )
    Row(
        modifier = Modifier
            .padding(2.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .drawBehind {
                    drawCircle(color = shapeColor)
                }
                .padding(4.dp)
        ) {
            Icon(
                imageVector = if (user.role == "USER") Icons.Default.AccountCircle else Icons.Default.SupervisorAccount,
                contentDescription = "User Icon",
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .height(animatedHeight)
        ) {
            Text(
                text = "${user.username} #${user.id}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = shapeColor,
                            cornerRadius = CornerRadius(x = 10.dp.toPx())
                        )
                    }
                    .padding(12.dp),
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(150.dp)
                    .padding(4.dp)
            )
            Text(
                text = user.createdAt.split("T").first(),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(150.dp)
                    .padding(4.dp)
            )
            Text(
                text = "${user.createdAt.split("T").last().subSequence(0, 8)}",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(150.dp)
                    .padding(4.dp)
            )
        }

    }
}

@Preview
@Composable
fun UserCartPreview() {
    MiniECommerceTheme {
        Surface {
            Column {
                UserCard(
                    isExpanded = true,
                    user = UserResponse(
                        12L,
                        "Jack",
                        "Johnathan",
                        "david@ag.com",
                        "ADMIN",
                        "2025/12/23T16:42:12:23323"
                    ),
                    onCardClick = {},
                    onEditUserClick = {},
                    onDeleteUserClick = {}
                )
                UserCard(
                    isExpanded = false,
                    user = UserResponse(
                        12L,
                        "Jack",
                        "Johnathan",
                        "david@ag.com",
                        "USER",
                        "2025/01/23T18:42:12:8272"
                    ),
                    onCardClick = {},
                    onEditUserClick = {},
                    onDeleteUserClick = {}
                )
            }
        }
    }
}