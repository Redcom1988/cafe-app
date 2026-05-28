package com.redcom1988.cafej3.model

// Merepresentasikan item yang masuk ke keranjang
data class OrderItem(
    val menuItem: MenuItem,
    val qty: Int,
    val notes: String = ""

)

fun Int.toRupiahFormat(): String {
    val inThousands = this / 1000
    return "Rp.${inThousands}K"
}

fun Double.toRupiahFormat(): String {
    val inThousands = (this / 1000).toInt()
    return "Rp.${inThousands}K"
}