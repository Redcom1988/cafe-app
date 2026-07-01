package com.redcom1988.cafej3.screens.menu

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.menu.interactor.GetCategories
import com.redcom1988.domain.menu.interactor.GetMenuItems
import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MenuUiState(
    val categories: List<MenuCategory> = emptyList(),
    val items: List<MenuItem> = emptyList(),
    val selectedCategoryId: Int? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MenuScreenModel(
    private val getCategories: GetCategories = inject(),
    private val getMenuItems: GetMenuItems = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    private var allItems: List<MenuItem> = emptyList()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val categories = getCategories.await()
                val items = getMenuItems.await()
                allItems = items
                _state.value = _state.value.copy(
                    categories = categories,
                    items = items,
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

    fun selectCategory(categoryId: Int?) {
        _state.value = _state.value.copy(selectedCategoryId = categoryId, isLoading = true, error = null)
        screenModelScope.launch {
            try {
                val items = getMenuItems.await(categoryId = categoryId)
                allItems = items
                applyFilters()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun toggleSearch() {
        val active = !_state.value.isSearchActive
        _state.value = _state.value.copy(isSearchActive = active, searchQuery = "")
        if (!active) applyFilters()
    }

    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun applyFilters() {
        val s = _state.value
        var filtered = allItems
        val query = s.searchQuery.trim().lowercase()
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                it.category.name.lowercase().contains(query)
            }
        }
        _state.value = s.copy(items = filtered)
    }
}
