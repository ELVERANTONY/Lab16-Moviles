package com.tecsup.lab16.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lab16.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<Result<Boolean>?>(null)
    val authState: StateFlow<Result<Boolean>?> = _authState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = repository.login(email, pass)
        }
    }

    fun register(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = repository.register(email, pass)
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = null
    }

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }
}
