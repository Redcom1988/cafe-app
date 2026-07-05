package com.redcom1988.cafej3.screens.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.auth.interactor.GetCurrentUser
import com.redcom1988.domain.auth.interactor.Logout
import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.table.interactor.TableSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

class ProfileScreenModel(
    private val getCurrentUser: GetCurrentUser = inject(),
    private val tableSession: TableSession = inject(),
    private val logout: Logout = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun refresh() {
        screenModelScope.launch {
            _state.value = ProfileUiState(isRefreshing = true)
            try {
                val user = getCurrentUser.await()
                _state.value = ProfileUiState(user = user)
            } catch (e: Exception) {
                _state.value = ProfileUiState(error = e.message ?: "Failed to load profile")
            }
        }
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

    fun logout() {
        screenModelScope.launch {
            logout.await()
            tableSession.clear()
        }
    }
}
