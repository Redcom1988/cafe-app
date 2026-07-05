package com.redcom1988.cafej3.screens.scan

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.network.NetworkPreference
import com.redcom1988.core.util.inject
import com.redcom1988.domain.table.interactor.ScanTable
import com.redcom1988.domain.table.interactor.TableSession
import com.redcom1988.domain.table.model.CafeTable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScanUiState(
    val scannedCode: String? = null,
    val table: CafeTable? = null,
    val isVerifying: Boolean = false,
    val showManualInput: Boolean = false,
    val manualTableNumber: String = "",
    val error: String? = null
)

class ScanCheckInScreenModel(
    private val scanTable: ScanTable = inject(),
    private val tableSession: TableSession = inject(),
    private val networkPreference: NetworkPreference = inject()
) : ScreenModel {

    val isLoggedIn: Boolean get() = networkPreference.accessToken().get().isNotBlank()

    private val _state = MutableStateFlow(ScanUiState())
    val state: StateFlow<ScanUiState> = _state.asStateFlow()

    fun onQrScanned(qrCode: String) {
        if (_state.value.isVerifying || _state.value.table != null) return
        _state.value = ScanUiState(scannedCode = qrCode, isVerifying = true)
        screenModelScope.launch {
            try {
                val table = scanTable.await(qrCode)
                tableSession.setTable(table.id, table.tableNumber)
                _state.value = ScanUiState(scannedCode = qrCode, table = table)
            } catch (e: Exception) {
                _state.value = ScanUiState(
                    scannedCode = qrCode,
                    error = e.message ?: "Invalid QR code"
                )
            }
        }
    }

    fun toggleManualInput() {
        _state.value = _state.value.copy(showManualInput = !_state.value.showManualInput, manualTableNumber = "")
    }

    fun onManualTableNumberChange(value: String) {
        _state.value = _state.value.copy(manualTableNumber = value)
    }

    fun submitManualTable() {
        val number = _state.value.manualTableNumber.trim()
        if (number.isBlank()) return
        val qrCode = "JASJISJUS-TABLE-${number.padStart(2, '0')}"
        onQrScanned(qrCode)
    }

    fun reset() {
        _state.value = ScanUiState()
    }

}
