package com.redcom1988.cafej3.screens.rewards

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.offer.interactor.GetOffers
import com.redcom1988.domain.offer.interactor.RedeemOffer
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
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val redeemingOfferId: Int? = null,
    val redeemSuccess: Boolean = false,
    val redeemError: String? = null
)

class RewardsScreenModel(
    private val getPointBalance: GetPointBalance = inject(),
    private val getPointHistory: GetPointHistory = inject(),
    private val getOffers: GetOffers = inject(),
    private val redeemOffer: RedeemOffer = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun refresh() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, error = null)
            try {
                val balance = getPointBalance.await()
                val history = getPointHistory.await()
                val offers = getOffers.await()
                _state.value = _state.value.copy(
                    balance = balance,
                    pointsHistory = history,
                    offers = offers,
                    isRefreshing = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    error = e.message
                )
            }
        }
    }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val balance = getPointBalance.await()
                val history = getPointHistory.await()
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

    fun redeem(offerId: Int) {
        screenModelScope.launch {
            _state.value = _state.value.copy(redeemingOfferId = offerId, redeemError = null, redeemSuccess = false)
            try {
                redeemOffer.await(offerId)
                val balance = getPointBalance.await()
                _state.value = _state.value.copy(
                    balance = balance,
                    redeemingOfferId = null,
                    redeemSuccess = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    redeemingOfferId = null,
                    redeemError = e.message ?: "Failed to redeem offer"
                )
            }
        }
    }

    fun dismissRedeemSuccess() {
        _state.value = _state.value.copy(redeemSuccess = false)
    }

    fun dismissRedeemError() {
        _state.value = _state.value.copy(redeemError = null)
    }
}
