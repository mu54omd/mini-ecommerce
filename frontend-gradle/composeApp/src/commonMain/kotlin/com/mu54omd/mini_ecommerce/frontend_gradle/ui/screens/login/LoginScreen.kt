package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.AppThemeExtras

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var isUsernameWrong by remember { mutableStateOf(false) }
    var isPasswordWrong by remember { mutableStateOf(false) }
    var isEmailWrong by remember { mutableStateOf(false) }

    var isPasswordVisible by remember { mutableStateOf(false) }

    var usernameWrongMessage by remember { mutableStateOf("") }
    var passwordWrongMessage by remember { mutableStateOf("") }
    var emailWrongMessage by remember { mutableStateOf("") }

    val state by authViewModel.state.collectAsState()
    val effect = authViewModel.effect

    var alertMessage by remember { mutableStateOf<String?>(null) }

    var haveAnAccount by remember { mutableStateOf(true) }


    val lineBrush = AppThemeExtras.brushes.lineBrush

    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }

    fun resetLoginScreenTextField() {
        isEmailWrong = false
        isUsernameWrong = false
        isPasswordWrong = false
        isPasswordVisible = false
        usernameWrongMessage = ""
        passwordWrongMessage = ""
        emailWrongMessage = ""
    }

    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                is AuthUiEffect.ShowError -> {
                    alertMessage = effect.message
                }

                is AuthUiEffect.ShowMessage -> {
                    alertMessage = effect.message
                }
                else -> Unit
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().imePadding()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.width(300.dp).height(600.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(usernameFocusRequester),
                isError = isUsernameWrong,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username Field Icon"
                    )
                },
                trailingIcon = {
                    if (username.isNotBlank()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape)
                                .clickable { username = "" }
                                .pointerHoverIcon(PointerIcon.Hand),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Username Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                },
                supportingText = {
                    if (isUsernameWrong) {
                        Text(
                            text = usernameWrongMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                },
                shape = RoundedCornerShape(30),
                textStyle = TextStyle(brush = lineBrush),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                )
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (!isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                isError = isPasswordWrong,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Password Field Icon"
                    )
                },
                trailingIcon = {
                    if (password.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.width(100.dp).padding(end = 4.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .clickable { isPasswordVisible = !isPasswordVisible }
                                    .pointerHoverIcon(PointerIcon.Hand),
                            ) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Clear Password Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .clickable { password = "" }
                                    .pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Password Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }

                    }
                },
                supportingText = {
                    if (isPasswordWrong) {
                        Text(
                            text = passwordWrongMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                },
                shape = RoundedCornerShape(30),
                textStyle = TextStyle(brush = lineBrush),
                keyboardOptions = KeyboardOptions(imeAction = if (haveAnAccount) ImeAction.Done else ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (username.isNotBlank() && password.isNotBlank()) {
                            resetLoginScreenTextField()
                            if (haveAnAccount) {
                                authViewModel.login(username, password)
                            }
                        }
                    },
                    onNext = {
                        emailFocusRequester.requestFocus()
                    }
                )
            )
            AnimatedVisibility(!haveAnAccount) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocusRequester),
                    isError = isEmailWrong,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = "Email Field Icon"
                        )
                    },
                    trailingIcon = {
                        if (email.isNotBlank()) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .clickable { email = "" }
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .focusable(false)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Email Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    },
                    supportingText = {
                        if (isEmailWrong) {
                            Text(
                                text = emailWrongMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    },
                    shape = RoundedCornerShape(30),
                    textStyle = TextStyle(brush = lineBrush),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (username.isNotBlank() && password.isNotBlank() && email.isNotBlank()) {
                                resetLoginScreenTextField()
                                if (!haveAnAccount) {
                                    authViewModel.register(username, password, email)
                                }
                            }
                        }
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = !state.isRefreshing,
                    onClick = {
                        resetLoginScreenTextField()
                        if (haveAnAccount) {
                            authViewModel.login(username, password)
                        } else {
                            authViewModel.register(username, password, email)
                        }
                    },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    AnimatedContent(targetState = haveAnAccount) { state ->
                        when (state) {
                            true -> Text("Login")
                            false -> Text("Register")
                        }
                    }
                }
                TextButton(
                    enabled = !state.isRefreshing,
                    onClick = {
                        resetLoginScreenTextField()
                        haveAnAccount = !haveAnAccount
                    },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    AnimatedContent(targetState = haveAnAccount) { state ->
                        when (state) {
                            false -> Text("I have an account!")
                            true -> Text("Don't have an account!")
                        }
                    }
                }
            }
            TextButton(
                enabled = !state.isRefreshing,
                modifier = Modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand),
                onClick = {
                    resetLoginScreenTextField()
                    authViewModel.checkHealth()
                }
            ) {
                Text("Login as guest!")
            }
        }

        alertMessage?.let {
            AlertModal(
                message = it,
                onConfirmClick = { alertMessage = null }
            )
        }
    }
}
