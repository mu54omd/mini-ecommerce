package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditUserDialog(
    user: UserResponse,
    onCancelClick: () -> Unit,
    onConfirmClick: (UserEditRequest) -> Unit,
) {
    var username by remember { mutableStateOf(user.username) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user.email) }
    var role by remember { mutableStateOf(user.role) }

    Dialog(
        onDismissRequest = onCancelClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(5)
            ).width(300.dp).height(400.dp).padding(8.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Type Your New Password")},
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("User:")
                RadioButton(selected = role == "USER", onClick = { role = "USER" })
                Spacer(modifier = Modifier.width(20.dp))
                Text("Admin:")
                RadioButton(selected = role == "ADMIN", onClick = { role = "ADMIN" })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                TextButton(onClick =
                    {
                        onConfirmClick(
                            UserEditRequest(
                                username = username,
                                email = email,
                                password = password,
                                role = role
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Confirm Edit"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Confirm")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onCancelClick) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel Edit"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Cancel")
                }
            }
        }
    }
}


@Composable
@Preview
fun EditUserDialogPreview() {
    MaterialTheme {
        EditUserDialog(
            user = UserResponse(
                id = 2,
                "musa",
                "8238232839",
                "aaa@iii.com",
                "USER",
                "47834747384837"
            ),
            onCancelClick = {},
            onConfirmClick = {}
        )
    }
}