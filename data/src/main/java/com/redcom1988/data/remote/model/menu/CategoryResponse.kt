package com.redcom1988.data.remote.model.menu

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val menuCategoryId: Int,
    val name: String
)
