package com.redcom1988.domain.menu.model

data class MenuItem(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val imageUrl: String? = null,
    val isAvailable: Boolean = true,
    val price: String,
    val category: MenuCategory
)
