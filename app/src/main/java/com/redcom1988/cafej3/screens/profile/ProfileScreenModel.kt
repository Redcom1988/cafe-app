package com.redcom1988.cafej3.screens.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.auth.interactor.GetCurrentUser
import com.redcom1988.domain.auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileScreenModel(
    private val getCurrentUser: GetCurrentUser = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = ProfileUiState(isLoading = true)
            try {
                val user = getCurrentUser.await()
                _state.value = ProfileUiState(user = user)
            } catch (e: Exception) {
                _state.value = ProfileUiState(error = e.message ?: "Failed to load profile")
            }
        }
    }
}
