package com.redcom1988.cafej3.model

data class MenuItem(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: Int = 0,
    val isSpecial: Boolean = false,
    val isPopular: Boolean = false
)