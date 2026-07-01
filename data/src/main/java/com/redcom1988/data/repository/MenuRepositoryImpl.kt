package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.menu.CategoryResponse
import com.redcom1988.data.remote.model.menu.MenuItemResponse
import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.model.MenuItem
import com.redcom1988.domain.menu.repository.MenuRepository

class MenuRepositoryImpl(
    private val api: Cafej3Api
) : MenuRepository {

    override suspend fun getCategories(): List<MenuCategory> {
        val response = api.getCategories()
        if (!response.isSuccessful) throw Exception("Failed to fetch categories (HTTP ${response.code})")
        return response.parseAs<List<CategoryResponse>>().map { it.toDomain() }
    }

    override suspend fun getMenuItems(categoryId: Int?, search: String?): List<MenuItem> {
        val response = api.getMenuItems(categoryId, search)
        if (!response.isSuccessful) throw Exception("Failed to fetch menu items (HTTP ${response.code})")
        return response.parseAs<List<MenuItemResponse>>().map { it.toDomain() }
    }

    override suspend fun getMenuItemDetail(id: Int): MenuItem {
        val response = api.getMenuItemDetail(id)
        if (!response.isSuccessful) throw Exception("Failed to fetch menu item (HTTP ${response.code})")
        return response.parseAs<MenuItemResponse>().toDomain()
    }
}
