package com.redcom1988.core.network

import com.redcom1988.core.preference.PreferenceStore

class NetworkPreference(
    private val preferenceStore: PreferenceStore
) {

    companion object {
        private const val DEV_BASE_URL = "http://100.108.202.107:3000"
        private const val PROD_BASE_URL = "https://backend-cafe-jasjisjus-production.up.railway.app"

        // Devs: toggle this to DEV_BASE_URL for local development
        private const val BASE_URL = DEV_BASE_URL
    }

    fun accessToken() = preferenceStore.getString(
        key = "access_token",
        defaultValue = ""
    )

    fun refreshToken() = preferenceStore.getString(
        key = "refresh_token",
        defaultValue = ""
    )

    fun baseUrl() = preferenceStore.getString(
        key = "api_base_url",
        defaultValue = BASE_URL
    )
}