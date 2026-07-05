package com.redcom1988.domain.table.interactor

import com.redcom1988.domain.preference.ApplicationPreference

class TableSession(
    private val applicationPreference: ApplicationPreference
) {
    fun getTableId(): Int = applicationPreference.tableId().get()

    fun getTableNumber(): String {
        if (!hasTable()) return ""
        return applicationPreference.tableNumber().get().takeIf { it.isNotBlank() }
            ?: "Table #${getTableId()}"
    }

    fun hasTable(): Boolean = getTableId() > 0

    fun setTable(id: Int, number: String) {
        applicationPreference.tableId().set(id)
        applicationPreference.tableNumber().set(number)
    }

    fun getActiveOrderId(): Int = applicationPreference.activeOrderId().get()

    fun hasActiveOrder(): Boolean = getActiveOrderId() > 0

    fun setActiveOrderId(id: Int) {
        applicationPreference.activeOrderId().set(id)
    }

    fun clearActiveOrder() {
        applicationPreference.activeOrderId().set(0)
    }

    fun getTrackingToken(): String = applicationPreference.trackingToken().get()

    fun hasTrackingToken(): Boolean = getTrackingToken().isNotBlank()

    fun setTrackingToken(token: String) {
        applicationPreference.trackingToken().set(token)
    }

    fun clearTrackingToken() {
        applicationPreference.trackingToken().set("")
    }

    fun clear() {
        applicationPreference.clearTable()
    }

    fun clearAll() {
        applicationPreference.clearAll()
    }
}
