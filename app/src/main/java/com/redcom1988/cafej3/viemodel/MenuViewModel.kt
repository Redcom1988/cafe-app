package com.redcom1988.cafej3.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.redcom1988.cafej3.data.DummyData
import com.redcom1988.cafej3.model.MenuItem

class MenuViewModel : ViewModel() {

    private val _allItems = mutableStateListOf(*DummyData.menuItems.toTypedArray())

    val categories = DummyData.categories

    val selectedCategory = mutableStateOf("Menu")

    val filteredItems: List<MenuItem>
        get() = if (selectedCategory.value == "Menu") _allItems.toList()
        else _allItems.filter { it.category == selectedCategory.value }

    fun addItem(item: MenuItem) {
        _allItems.add(item)
    }

    fun updateItem(updated: MenuItem) {
        val index = _allItems.indexOfFirst { it.id == updated.id }
        if (index != -1) _allItems[index] = updated
    }

    fun deleteItem(id: Int) {
        _allItems.removeIf { it.id == id }
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }
}