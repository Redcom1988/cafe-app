package com.redcom1988.domain.preference

import com.redcom1988.core.preference.PreferenceStore
import com.redcom1988.core.preference.getEnum
import com.redcom1988.domain.theme.Themes

class ApplicationPreference(
    private val preferenceStore: PreferenceStore
) {

    fun appTheme() = preferenceStore.getEnum(
        key = "app_theme",
        defaultValue = Themes.SYSTEM,
    )

    fun tableId() = preferenceStore.getInt(
        key = "table_id",
        defaultValue = 0,
    )

    fun tableNumber() = preferenceStore.getString(
        key = "table_number",
        defaultValue = "",
    )

    fun activeOrderId() = preferenceStore.getInt(
        key = "active_order_id",
        defaultValue = 0,
    )

    fun trackingToken() = preferenceStore.getString(
        key = "tracking_token",
        defaultValue = "",
    )

    fun clearTable() {
        tableId().set(0)
        tableNumber().set("")
    }

    fun clearAll() {
        tableId().set(0)
        tableNumber().set("")
        activeOrderId().set(0)
        trackingToken().set("")
    }

}