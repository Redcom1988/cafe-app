package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.menu.CategoryResponse
import com.redcom1988.data.remote.model.menu.MenuItemResponse
import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.model.MenuItem

fun CategoryResponse.toDomain(): MenuCategory = MenuCategory(
    id = menuCategoryId,
    name = name
)

fun MenuItemResponse.toDomain(): MenuItem = MenuItem(
    id = menuItemId,
    categoryId = menuCategoryId,
    name = name,
    imageUrl = imageUrl,
    isAvailable = isAvailable,
    price = price,
    category = category.toDomain()
)
