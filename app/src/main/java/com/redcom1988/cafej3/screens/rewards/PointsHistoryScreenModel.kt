package com.redcom1988.cafej3.screens.rewards

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.points.interactor.GetPointHistory
import com.redcom1988.domain.points.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PointsHistoryUiState(
    val points: List<Point> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PointsHistoryScreenModel(
    private val getPointHistory: GetPointHistory = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(PointsHistoryUiState())
    val state: StateFlow<PointsHistoryUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = PointsHistoryUiState(isLoading = true)
            try {
                val points = getPointHistory.await()
                _state.value = PointsHistoryUiState(points = points)
            } catch (e: Exception) {
                _state.value = PointsHistoryUiState(error = e.message ?: "Failed to load points history")
            }
        }
    }
}
