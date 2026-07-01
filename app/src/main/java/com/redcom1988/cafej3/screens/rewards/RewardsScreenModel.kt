package com.redcom1988.cafej3.screens.rewards

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.offer.interactor.GetOffers
import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.points.interactor.GetPointBalance
import com.redcom1988.domain.points.interactor.GetPointHistory
import com.redcom1988.domain.points.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RewardsUiState(
    val balance: Int = 0,
    val pointsHistory: List<Point> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RewardsScreenModel(
    private val getPointBalance: GetPointBalance = inject(),
    private val getPointHistory: GetPointHistory = inject(),
    private val getOffers: GetOffers = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load(userId: Int = 1) {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val balance = getPointBalance.await(userId)
                val history = getPointHistory.await(userId)
                val offers = getOffers.await()
                _state.value = RewardsUiState(
                    balance = balance,
                    pointsHistory = history,
                    offers = offers,
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
