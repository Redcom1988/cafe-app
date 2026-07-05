package com.redcom1988.cafej3.screens.cart

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.offer.interactor.GetUserOffers
import com.redcom1988.domain.offer.model.UserOffer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OfferGroup(
    val offerName: String,
    val count: Int,
    val firstUserOfferId: Int
)

data class OfferSelectionUiState(
    val groups: List<OfferGroup> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OfferSelectionScreenModel(
    private val getUserOffers: GetUserOffers = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(OfferSelectionUiState())
    val state: StateFlow<OfferSelectionUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = OfferSelectionUiState(isLoading = true)
            try {
                val offers = getUserOffers.await()
                val available = offers.filter { it.status == "AVAILABLE" }
                val groups = available.groupBy { it.offerId }.map { (_, userOffers) ->
                    val first = userOffers.first()
                    OfferGroup(
                        offerName = first.offer?.name ?: "Offer #${first.offerId}",
                        count = userOffers.size,
                        firstUserOfferId = first.id
                    )
                }
                _state.value = OfferSelectionUiState(groups = groups)
            } catch (e: Exception) {
                _state.value = OfferSelectionUiState(error = e.message ?: "Failed to load offers")
            }
        }
    }

    fun select(userOfferId: Int) {
        OfferSelectionStore.pendingUserOfferId = userOfferId
    }
}
