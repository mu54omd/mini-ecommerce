package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAsGuest: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val loginState by authViewModel.tokenState.collectAsState()
    val registerState by authViewModel.registerState.collectAsState()
    var haveAnAccount by remember { mutableStateOf(true)}
    val scope = rememberCoroutineScope()

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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if(!haveAnAccount) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = loginState !is UiState.Loading,
                    onClick = {
                        authViewModel.reset()
                        if(haveAnAccount) {
                            authViewModel.login(username, password)
                        }else {
                            authViewModel.register(username, password, email)
                        }
                    }
                ) {
                    if(haveAnAccount) Text("Login") else Text("Register")
                }
                TextButton(
                    onClick = {
                        authViewModel.reset()
                        haveAnAccount = !haveAnAccount
                    }
                ) {
                    if(!haveAnAccount) Text("I have an account!") else Text("Don't have an account!")
                }
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginAsGuest
            ) {
                Text("Login as guest!")
            }
            Spacer(Modifier.height(8.dp))
            when (loginState) {
                is UiState.Idle -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> {
                    Text(
                        text = "Error: ${(loginState as UiState.Error).message}",
                        color = Color.Red,
                    )
                    println("Error: ${(loginState as UiState.Error).message}")
                }
                is UiState.Success -> {
                    Text(text = "Welcome!")
                    scope.launch {
                        delay(500)
                        onLoginSuccess()
                    }
                }

                is UiState.Unauthorized -> Text("Invalid credentials", color = Color.Red)
            }
            when (registerState) {
                is UiState.Idle -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> {
                    Text(
                        text = "Error: ${(registerState as UiState.Error).message}",
                        color = Color.Red,
                    )
                    println("Error: ${(registerState as UiState.Error).message}")
                }

                is UiState.Success -> {
                    Text(text = (registerState as UiState.Success<RegisterResponse>).data.message)
                    haveAnAccount = true
                    email = ""
                    password = ""
                    scope.launch {
                        delay(1000)
                        authViewModel.reset()
                    }
                }

                is UiState.Unauthorized -> Text("Invalid credentials", color = Color.Red)
            }
        }
    }
}