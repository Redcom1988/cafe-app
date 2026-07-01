package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.order.OrderItemResponse
import com.redcom1988.data.remote.model.order.OrderResponse
import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.model.OrderItem

fun OrderResponse.toDomain(): Order = Order(
    id = orderId,
    orderCode = orderCode,
    customerName = customerName,
    status = status,
    totalPrice = totalPrice,
    finalPrice = finalPrice,
    items = orderItems.map { it.toDomain() },
    createdAt = createdAt
)

fun OrderItemResponse.toDomain(): OrderItem = OrderItem(
    id = orderItemId,
    menuItemId = menuItemId,
    quantity = quantity,
    itemPriceAtOrder = itemPriceAtOrder,
    subtotal = subtotal,
    note = note
)
