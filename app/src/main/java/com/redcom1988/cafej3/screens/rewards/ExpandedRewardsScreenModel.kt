package com.redcom1988.cafej3.screens.rewards

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.offer.interactor.GetOffers
import com.redcom1988.domain.offer.interactor.RedeemOffer
import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.points.interactor.GetPointBalance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExpandedRewardsUiState(
    val offers: List<Offer> = emptyList(),
    val balance: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val redeemingOfferId: Int? = null,
    val redeemSuccess: Boolean = false,
    val redeemError: String? = null
)

class ExpandedRewardsScreenModel(
    private val getOffers: GetOffers = inject(),
    private val getPointBalance: GetPointBalance = inject(),
    private val redeemOffer: RedeemOffer = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(ExpandedRewardsUiState())
    val state: StateFlow<ExpandedRewardsUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val balance = getPointBalance.await()
                val offers = getOffers.await()
                _state.value = ExpandedRewardsUiState(
                    offers = offers,
                    balance = balance,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load rewards"
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
