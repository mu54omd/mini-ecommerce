package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.DeleteModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components.AddEditUserModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components.UserCard

@Composable
fun UsersScreen(
    userViewModel: UserViewModel,
    addUserModalState: Boolean,
    onAddUserStateChange: (Boolean) -> Unit,
    onExit: (String?) -> Unit,
) {

    val state by userViewModel.state.collectAsState()
    val effect = userViewModel.effect

    var editUserModalState by remember { mutableStateOf(false) }
    var deleteUserModalState by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf<String?>(null) }

    var editUserRequest by remember { mutableStateOf(UserResponse()) }
    var selectedUserForDelete by remember { mutableLongStateOf(-1) }

    var expandedUsers by rememberSaveable { mutableStateOf<Map<Long, Boolean>>(emptyMap()) }

    LaunchedEffect(Unit) {
        userViewModel.getAllUsers()
    }

    LaunchedEffect(Unit) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is UserUiEffect.ShowMessage -> { alertMessage = uiEffect.message }
                is UserUiEffect.ShowError -> { alertMessage = uiEffect.message }
                is UserUiEffect.NavigateToLogin -> { onExit(uiEffect.message) }
            }
        }
    }

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 50.dp),
        ) {
            items(items = state.users, key = { user -> user.id }) { user ->
                val isExpanded = expandedUsers[user.id] ?: false
                UserCard(
                    isExpanded = isExpanded,
                    user = user,
                    onCardClick = {
                        expandedUsers = expandedUsers.toMutableMap().also {
                            val current = it[user.id] ?: false
                            it[user.id] = !current
                        }
                    },
                    onEditUserClick = {
                        editUserRequest = user
                        editUserModalState = true
                    },
                    onDeleteUserClick = {
                        deleteUserModalState = true
                        selectedUserForDelete = user.id
                    }
                )
            }
        }

        if (editUserModalState) {
            AddEditUserModal(
                user = editUserRequest,
                onCancelClick = { editUserModalState = false },
                onConfirmClick = { user ->
                    userViewModel.editUser(editUserRequest.id, user)
                    editUserModalState = false
                }
            )
        }
        if (addUserModalState) {
            AddEditUserModal(
                user = UserResponse(),
                onCancelClick = { onAddUserStateChange(false) },
                onConfirmClick = { user ->
                    userViewModel.createUser(user)
                    onAddUserStateChange(false)
                }
            )
        }

        if (deleteUserModalState) {
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
        alertMessage?.let {
            AlertModal(
                message = it,
                onConfirmClick = {
                    alertMessage = null
                }
            )
        }
    }
}