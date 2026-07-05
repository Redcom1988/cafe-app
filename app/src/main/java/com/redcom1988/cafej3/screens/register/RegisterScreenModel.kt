package com.redcom1988.cafej3.screens.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.auth.interactor.Register
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterScreenModel(
    private val register: Register = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onUsernameChange(value: String) {
        _state.value = _state.value.copy(username = value, error = null)
    }

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(name = value, error = null)
    }

    fun onPhoneChange(value: String) {
        _state.value = _state.value.copy(phoneNumber = value, error = null)
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.username.isBlank() || s.email.isBlank() || s.password.isBlank() || s.name.isBlank()) {
            _state.value = s.copy(error = "All fields are required")
            return
        }
        if (s.password.length < 6) {
            _state.value = s.copy(error = "Password must be at least 6 characters")
            return
        }

        screenModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)
            try {
                register.await(
                    username = s.username.trim(),
                    email = s.email.trim(),
                    password = s.password,
                    name = s.name.trim(),
                    phoneNumber = s.phoneNumber.trim().ifBlank { null }
                )
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                onSuccess()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed"
                )
            }
        }
    }
}
