package com.redcom1988.domain.menu.repository

import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.model.MenuItem
interface MenuRepository {
    suspend fun getCategories(): List<MenuCategory>
    suspend fun getMenuItems(categoryId: Int? = null, search: String? = null): List<MenuItem>
    suspend fun getMenuItemDetail(id: Int): MenuItem
}
