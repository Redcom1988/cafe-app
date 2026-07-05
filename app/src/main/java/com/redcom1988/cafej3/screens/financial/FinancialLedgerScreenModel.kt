package com.redcom1988.cafej3.screens.financial

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.analytics.interactor.GetDailySummary
import com.redcom1988.domain.analytics.interactor.GetSales
import com.redcom1988.domain.analytics.interactor.GetTopItems
import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.model.TopItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FinancialUiState(
    val salesSummary: SalesSummary? = null,
    val dailySummary: DailySummary? = null,
    val topItems: List<TopItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FinancialLedgerScreenModel(
    private val getSales: GetSales = inject(),
    private val getDailySummary: GetDailySummary = inject(),
    private val getTopItems: GetTopItems = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(FinancialUiState())
    val state: StateFlow<FinancialUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val sales = getSales.await()
                val daily = getDailySummary.await()
                val top = getTopItems.await()
                _state.value = FinancialUiState(
                    salesSummary = sales,
                    dailySummary = daily,
                    topItems = top,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
