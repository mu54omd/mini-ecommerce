package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAsGuest: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var isUsernameWrong by remember { mutableStateOf(false) }
    var isPasswordWrong by remember { mutableStateOf(false) }
    var isEmailWrong by remember { mutableStateOf(false) }

    var usernameWrongMessage by remember { mutableStateOf("") }
    var passwordWrongMessage by remember { mutableStateOf("") }
    var emailWrongMessage by remember { mutableStateOf("") }

    val healthStatus by authViewModel.healthState.collectAsState()
    val loginState by authViewModel.tokenState.collectAsState()
    val registerState by authViewModel.registerState.collectAsState()
    var haveAnAccount by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun resetLoginScreenTextField() {
        isEmailWrong = false
        isUsernameWrong = false
        isPasswordWrong = false
        usernameWrongMessage = ""
        passwordWrongMessage = ""
        emailWrongMessage = ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
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
                modifier = Modifier.fillMaxWidth(),
                isError = isUsernameWrong,
                supportingText = {
                    if (isUsernameWrong) {
                        Text(
                            text = usernameWrongMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = isPasswordWrong,
                supportingText = {
                    if (isPasswordWrong) {
                        Text(
                            text = passwordWrongMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            )
            AnimatedVisibility(!haveAnAccount) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmailWrong,
                    supportingText = {
                        if (isEmailWrong) {
                            Text(
                                text = emailWrongMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = loginState !is UiState.Loading && healthStatus !is UiState.Loading && registerState !is UiState.Loading,
                    onClick = {
                        resetLoginScreenTextField()
                        authViewModel.resetAllStates()
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
                    enabled = loginState !is UiState.Loading && healthStatus !is UiState.Loading && registerState !is UiState.Loading,
                    onClick = {
                        resetLoginScreenTextField()
                        authViewModel.resetAllStates()
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
                enabled = loginState !is UiState.Loading && healthStatus !is UiState.Loading && registerState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand),
                onClick = {
                    resetLoginScreenTextField()
                    authViewModel.resetAllStates()
                    authViewModel.checkHealth()
                }
            ) {
                Text("Login as guest!")
            }
            Spacer(Modifier.height(8.dp))

            when(healthStatus){
                is UiState.Idle -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> {
                    Text(
                        text = (healthStatus as UiState.Error).message,
                        color = Color.Red,
                    )
                }
                is UiState.Success -> {
                    Text(text = "Welcome!")
                    scope.launch {
                        delay(1000)
                        onLoginAsGuest()
                    }

                }
            }
            when (loginState) {
                is UiState.Idle -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> {
                    Text(
                        text = (loginState as UiState.Error).message,
                        color = Color.Red,
                    )
                }

                is UiState.Success -> {
                    Text(text = "Welcome!")
                    scope.launch {
                        delay(1000)
                        onLoginSuccess()
                    }
                }
            }
            when (registerState) {
                is UiState.Idle -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> {
                    (registerState as UiState.Error).let { (message, fields) ->
                        when (message) {
                            "Username already exists" -> {
                                isUsernameWrong = true
                                usernameWrongMessage = message
                            }

                            "Email already exists" -> {
                                isEmailWrong = true
                                emailWrongMessage = message
                            }

                            else -> {
                                Text(
                                    text = message,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                                fields?.forEach { (key, value) ->
                                    when (key) {
                                        "password" -> {
                                            isPasswordWrong = true
                                            passwordWrongMessage = value
                                        }

                                        "email" -> {
                                            isEmailWrong = true
                                            emailWrongMessage = value
                                        }

                                        "username" -> {
                                            isUsernameWrong = true
                                            usernameWrongMessage = value
                                        }

                                        else -> {
                                            Text(
                                                text = "${key}: $value",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Red,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    Text(text = (registerState as UiState.Success<RegisterResponse>).data.message)
                    haveAnAccount = true
                    email = ""
                    password = ""
                    scope.launch {
                        delay(1000)
                        authViewModel.resetAllStates()
                    }
                }
            }
        }
    }
}
