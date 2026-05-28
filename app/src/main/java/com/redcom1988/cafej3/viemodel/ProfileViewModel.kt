package com.redcom1988.cafej3.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.redcom1988.cafej3.model.UserProfile

class ProfileViewModel : ViewModel() {

    val profile = mutableStateOf(UserProfile())

    fun updateProfile(updated: UserProfile) {
        profile.value = updated
    }
}