package com.redcom1988.domain.menu.interactor

import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategories(
    private val menuRepository: MenuRepository
) {
    suspend fun await(): List<MenuCategory> = withContext(Dispatchers.IO) {
        menuRepository.getCategories()
    }
}
