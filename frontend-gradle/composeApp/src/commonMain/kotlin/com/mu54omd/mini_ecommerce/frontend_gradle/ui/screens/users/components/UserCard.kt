package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse

@Composable
fun UserCart(
    user: UserResponse,
    onEditUserClick: () -> Unit,
    onDeleteUserClick: () -> Unit,
){
    Card(
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = if (user.role == "ADMIN") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(10)
                )
        ) {
            Column(modifier = Modifier.padding(12.dp).align(Alignment.TopCenter)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "User ID: ${user.id}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20)
                        ).padding(4.dp),
                        color = if (user.role == "ADMIN") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = "Username: ${user.username}",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Email: ${user.email}",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Creation date: ${user.createdAt.split("T").first()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Creation Time: ${user.createdAt.split("T").last()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp).align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onEditUserClick() },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit User Icon"
                    )
                }
                IconButton(
                    onClick = { onDeleteUserClick() },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete User Icon"
                    )
                }
            }
        }
    }
}