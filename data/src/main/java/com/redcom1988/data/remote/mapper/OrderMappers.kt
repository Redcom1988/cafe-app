package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.order.OrderItemResponse
import com.redcom1988.data.remote.model.order.OrderResponse
import com.redcom1988.data.remote.model.order.PaymentResponse
import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.model.OrderItem
import com.redcom1988.domain.order.model.Payment

fun PaymentResponse.toDomain(): Payment = Payment(
    id = paymentId,
    status = status,
    paymentUrl = paymentUrl,
    amount = amount,
    expiresAt = expiresAt,
    paidAt = paidAt,
    createdAt = createdAt
)

fun OrderResponse.toDomain(): Order = Order(
    id = orderId,
    orderCode = orderCode,
    customerName = customerName,
    status = status,
    payment = payment?.toDomain(),
    totalPrice = totalPrice,
    finalPrice = finalPrice,
    items = orderItems.map { it.toDomain() },
    createdAt = createdAt,
    trackingToken = trackingToken,
    cancellationReason = cancellationReason
)

fun OrderItemResponse.toDomain(): OrderItem = OrderItem(
    id = orderItemId,
    menuItemId = menuItemId,
    quantity = quantity,
    itemPriceAtOrder = itemPriceAtOrder ?: "0",
    subtotal = subtotal,
    note = note,
    menuItemName = menuItem?.name,
    unitStatuses = orderItemUnits.map { it.status }
)
