package com.redcom1988.domain.menu.interactor

import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.repository.MenuRepository

class GetCategories(
    private val menuRepository: MenuRepository
) {
    suspend fun await(): List<MenuCategory> {
        return menuRepository.getCategories()
    }
}
