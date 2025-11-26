package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
        maxSwipe = 100.dp,
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
) {
    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 50.dp
    )
    val iconScale by animateFloatAsState(
        targetValue = if(isExpanded) 1f else 0.9f
    )
    val createdDate = user.createdAt.split("T").first()
    val createdTime = "${user.createdAt.split("T").last().subSequence(0, 8)}"
    Column(
        modifier = Modifier
            .height(animatedHeight)
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .graphicsLayer{
                        scaleX = iconScale
                        scaleY = iconScale
                    }
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
            Text(
                text = "${user.username} #${user.id}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = shapeColor,
                            cornerRadius = CornerRadius(x = 10.dp.toPx())
                        )
                    }
                    .padding(12.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .drawBehind {
                    drawRoundRect(
                        color = shapeColor,
                        cornerRadius = CornerRadius(x = 10.dp.toPx())
                    )
                }
        ) {
            DetailsUserCardItem(
                text = user.email,
                icon = Icons.Outlined.Mail,
                description = "User Email Address",
            )
            DetailsUserCardItem(
                text = createdDate,
                icon = Icons.Outlined.DateRange,
                description = "User Creation Date"
            )
            DetailsUserCardItem(
                text = createdTime,
                icon = Icons.Outlined.Timelapse,
                description = "User Creation Time"
            )
        }
    }
}

@Composable
fun DetailsUserCardItem(
    text: String,
    icon: ImageVector,
    description: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            tint = LocalContentColor.current.copy(alpha = 0.7f)
        )
        VerticalDivider(
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.height(25.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(150.dp)
                .padding(4.dp)
                .graphicsLayer{
                    alpha = 0.8f
                }
        )
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