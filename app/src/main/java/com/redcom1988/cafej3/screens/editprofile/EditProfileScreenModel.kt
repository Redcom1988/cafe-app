package com.redcom1988.cafej3.screens.editprofile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.auth.interactor.GetCurrentUser
import com.redcom1988.domain.auth.interactor.UpdateProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

class EditProfileScreenModel(
    private val getCurrentUser: GetCurrentUser = inject(),
    private val updateProfile: UpdateProfile = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(EditProfileUiState())
    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = EditProfileUiState(isLoading = true)
            try {
                val user = getCurrentUser.await()
                _state.value = EditProfileUiState(
                    name = user.name,
                    username = user.username,
                    email = user.email,
                    phoneNumber = user.phoneNumber ?: ""
                )
            } catch (e: Exception) {
                _state.value = EditProfileUiState(error = e.message ?: "Failed to load profile")
            }
        }
    }

    fun onNameChange(value: String) { _state.value = _state.value.copy(name = value) }
    fun onUsernameChange(value: String) { _state.value = _state.value.copy(username = value) }
    fun onEmailChange(value: String) { _state.value = _state.value.copy(email = value) }
    fun onPhoneChange(value: String) { _state.value = _state.value.copy(phoneNumber = value) }

    fun save(onSuccess: () -> Unit) {
        val s = _state.value
        screenModelScope.launch {
            _state.value = s.copy(isSaving = true, error = null)
            try {
                updateProfile.await(
                    name = s.name,
                    username = s.username,
                    email = s.email,
                    phoneNumber = s.phoneNumber.ifBlank { null }
                )
                _state.value = _state.value.copy(isSaving = false, saved = true)
                onSuccess()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = e.message ?: "Failed to save"
                )
            }
        }
    }
}
