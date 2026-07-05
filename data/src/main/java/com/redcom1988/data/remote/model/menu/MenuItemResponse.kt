package com.redcom1988.data.remote.model.menu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemResponse(
    val menuItemId: Int,
    val menuCategoryId: Int,
    val name: String,
    val imageUrl: String? = null,
    val isAvailable: Boolean = true,
    val price: String,
    val category: CategoryResponse
)
