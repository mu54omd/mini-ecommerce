package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EditUserDialog
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView

@Composable
fun UsersScreen(
    userViewModel: UserViewModel,
    onExit: (UiState<*>) -> Unit
) {

    val usersState = userViewModel.usersState.collectAsState().value
    val deleteUserState = userViewModel.deleteUserState.collectAsState().value
    val editUserState = userViewModel.editUserState.collectAsState().value

    var editUserDialogState by remember { mutableStateOf(false) }
    var editUserRequest by remember { mutableStateOf(UserResponse()) }

    LaunchedEffect(Unit) {
        userViewModel.getAllUsers()
    }

    when (usersState) {
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                LazyColumn {
                    items(items = usersState.data, key = { user -> user.id }) { user ->
                        Card(
                            modifier = Modifier.padding(4.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
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
                                        onClick = {
                                            editUserRequest = user
                                            editUserDialogState = true
                                        },
                                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit User Icon"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            userViewModel.deleteUser(user.id)
                                        },
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
                }
                AnimatedVisibility(visible = editUserDialogState) {
                    EditUserDialog(
                        user = editUserRequest,
                        onCancelClick = { editUserDialogState = false },
                        onConfirmClick = { user ->
                            userViewModel.editUser(editUserRequest.id, user)
                            editUserDialogState = false
                        }
                    )
                }
            }
        }

        else -> onExit(usersState)
    }

}