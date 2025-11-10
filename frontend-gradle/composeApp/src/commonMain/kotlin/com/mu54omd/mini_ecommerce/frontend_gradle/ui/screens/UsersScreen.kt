package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

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
import androidx.compose.runtime.mutableLongStateOf
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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EditUserModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.DeleteModal
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.delete_user_successful_alert
import frontend_gradle.composeapp.generated.resources.edit_user_successful_alert
import frontend_gradle.composeapp.generated.resources.error_alert
import org.jetbrains.compose.resources.stringResource

@Composable
fun UsersScreen(
    userViewModel: UserViewModel,
    onExit: (UiState<*>) -> Unit
) {

    val usersState = userViewModel.usersState.collectAsState().value
    val deleteUserState = userViewModel.deleteUserState.collectAsState().value
    val editUserState = userViewModel.editUserState.collectAsState().value

    var editUserModalState by remember { mutableStateOf(false) }
    var deleteUserModalState by remember { mutableStateOf(false) }
    var alertModalState by remember { mutableStateOf(false) }
    var editUserRequest by remember { mutableStateOf(UserResponse()) }
    var selectedUserForDelete by remember { mutableLongStateOf(-1) }

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
                                            editUserModalState = true
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
                                            deleteUserModalState = true
                                            selectedUserForDelete = user.id
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
                if(editUserModalState) {
                    EditUserModal(
                        user = editUserRequest,
                        onCancelClick = { editUserModalState = false },
                        onConfirmClick = { user ->
                            userViewModel.editUser(editUserRequest.id, user)
                            editUserModalState = false
                        }
                    )
                }

                if(deleteUserModalState){
                    DeleteModal(
                        id = selectedUserForDelete,
                        onCancelClick = {
                            deleteUserModalState = false
                            selectedUserForDelete = -1
                        },
                        onConfirmClick = {
                            deleteUserModalState = false
                            userViewModel.deleteUser(selectedUserForDelete)
                            selectedUserForDelete = -1
                        }
                    )
                }
                when(deleteUserState){
                    is UiState.Idle -> {}
                    is UiState.Loading -> {}
                    else -> {
                        alertModalState = true
                        if(alertModalState) {
                            AlertModal(
                                message =
                                    if(deleteUserState is UiState.Success) {
                                        stringResource( Res.string.delete_user_successful_alert)
                                    }else {
                                        stringResource( Res.string.error_alert)
                                    },
                                onConfirmClick = {
                                    alertModalState = false
                                    userViewModel.resetDeleteUserState()
                                    userViewModel.getAllUsers()
                                },
                            )
                        }
                    }
                }
                when(editUserState){
                    is UiState.Idle -> {}
                    is UiState.Loading -> {}
                    else -> {
                        alertModalState = true
                        if(alertModalState) {
                            AlertModal(
                                message =
                                    if(editUserState is UiState.Success) {
                                        stringResource( Res.string.edit_user_successful_alert)
                                    }else {
                                        stringResource( Res.string.error_alert)
                                    },
                                onConfirmClick = {
                                    alertModalState = false
                                    userViewModel.resetEditUserState()
                                    userViewModel.getAllUsers()
                                },
                            )
                        }
                    }
                }


            }
        }

        else -> onExit(usersState)
    }

}