package com.redcom1988.domain.menu.interactor

import com.redcom1988.domain.menu.model.MenuItem
import com.redcom1988.domain.menu.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMenuItems(
    private val menuRepository: MenuRepository
) {
    suspend fun await(categoryId: Int? = null, search: String? = null): List<MenuItem> = withContext(Dispatchers.IO) {
        menuRepository.getMenuItems(categoryId, search)
    }
}
