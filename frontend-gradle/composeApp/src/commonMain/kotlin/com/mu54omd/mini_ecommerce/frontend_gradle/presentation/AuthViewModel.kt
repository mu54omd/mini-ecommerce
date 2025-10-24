package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.JwtResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AuthRepository

class AuthViewModel(private val repo: AuthRepository): ViewModel() {

    suspend fun login(username: String, password: String): JwtResponse{
        return repo.login(username, password)
    }
}