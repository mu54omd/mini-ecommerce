package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.users.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserModal(
    user: UserResponse,
    onCancelClick: () -> Unit,
    onConfirmClick: (UserEditRequest) -> Unit,
) {
    var username by remember { mutableStateOf(user.username) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user.email) }
    var role by remember { mutableStateOf(user.role) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val userColor = MaterialTheme.colorScheme.tertiaryContainer
    val adminColor = MaterialTheme.colorScheme.errorContainer
    val nullColor = MaterialTheme.colorScheme.surfaceContainer

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = .5f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceBright,
                    shape = RoundedCornerShape(5)
                ).width(350.dp).height(400.dp).padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))

                ) {
                    Text(
                        text = "User",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color =  MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .drawBehind{
                                drawRect(
                                    color = if(role == "USER") userColor else nullColor
                                )
                            }
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .clickable{
                                role = "USER"
                            }
                            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)

                    )
                    Text(
                        text = "Admin",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .drawBehind{
                                drawRect(
                                    color = if(role == "ADMIN") adminColor else nullColor
                                )
                            }
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .clickable{
                                role = "ADMIN"
                            }
                            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)

                    )
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = "Username") },
                    singleLine = true,
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(30),
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Type Your New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(30)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    singleLine = true,
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(30)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.width(300.dp)
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onCancelClick()
                            }
                        },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel Edit"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick =
                            {
                                onConfirmClick(
                                    UserEditRequest(
                                        username = username,
                                        email = email,
                                        password = password,
                                        role = role
                                    )
                                )
                            },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Confirm Edit"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun EditUserDialogPreview() {
    MaterialTheme {
        EditUserModal(
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