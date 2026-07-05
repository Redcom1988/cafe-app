package com.redcom1988.domain.auth.interactor

import com.redcom1988.core.network.NetworkPreference

class Logout(
    private val networkPreference: NetworkPreference
) {
    fun await() {
        networkPreference.accessToken().delete()
    }
}
