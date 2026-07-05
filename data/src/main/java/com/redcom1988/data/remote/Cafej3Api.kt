package com.redcom1988.data.remote

import com.redcom1988.core.network.GET
import com.redcom1988.core.network.NetworkHelper
import com.redcom1988.core.network.NetworkPreference
import com.redcom1988.core.network.PATCH
import com.redcom1988.core.network.POST
import com.redcom1988.core.network.await
import com.redcom1988.core.network.json
import com.redcom1988.data.remote.model.auth.LoginRequest
import com.redcom1988.data.remote.model.auth.RegisterRequest
import com.redcom1988.data.remote.model.auth.UpdateProfileRequest
import com.redcom1988.data.remote.model.offer.ApplyOfferRequest
import com.redcom1988.data.remote.model.offer.RedeemOfferRequest
import com.redcom1988.data.remote.model.order.CreateOrderRequest
import com.redcom1988.data.remote.model.order.UpdateOrderStatusRequest
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class Cafej3Api(
    private val networkHelper: NetworkHelper,
    private val preference: NetworkPreference
) {
    private val baseUrl get() = preference.baseUrl().get()
    private val jsonMediaType = "application/json".toMediaType()

    private inline fun <reified T> jsonBody(obj: T) =
        json.encodeToString(obj).toRequestBody(jsonMediaType)

    // ── Auth ──
    suspend fun login(request: LoginRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/auth/login", body = jsonBody(request))
        ).await()
    }

    suspend fun register(request: RegisterRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/auth/register", body = jsonBody(request))
        ).await()
    }

    suspend fun me(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/auth/me")
        ).await()
    }

    // ── Users ──
    suspend fun updateProfile(request: UpdateProfileRequest): Response {
        return networkHelper.client.newCall(
            PATCH(url = "$baseUrl/users/me/profile", body = jsonBody(request))
        ).await()
    }

    // ── Menu ──
    suspend fun getCategories(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/menu/categories")
        ).await()
    }

    suspend fun getMenuItems(categoryId: Int? = null, search: String? = null): Response {
        val url = buildString {
            append("$baseUrl/menu/items")
            val params = mutableListOf<String>()
            categoryId?.let { params.add("categoryId=$it") }
            search?.let { params.add("search=$it") }
            if (params.isNotEmpty()) {
                append("?${params.joinToString("&")}")
            }
        }
        return networkHelper.client.newCall(GET(url = url)).await()
    }

    suspend fun getMenuItemDetail(id: Int): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/menu/items/$id")
        ).await()
    }

    // ── Tables ──
    suspend fun getTableByQrCode(qrCode: String): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/tables/$qrCode")
        ).await()
    }

    // ── Orders ──
    suspend fun createOrder(request: CreateOrderRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/orders", body = jsonBody(request))
        ).await()
    }

    suspend fun createAuthenticatedOrder(request: CreateOrderRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/orders/me", body = jsonBody(request))
        ).await()
    }

    suspend fun trackGuestOrder(id: Int, trackingToken: String): Response {
        val headers = Headers.Builder().add("X-Tracking-Token", trackingToken).build()
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/orders/$id/track", headers = headers)
        ).await()
    }

    suspend fun getOrders(status: String? = null): Response {
        val url = if (status != null) "$baseUrl/orders?status=$status" else "$baseUrl/orders"
        return networkHelper.client.newCall(GET(url = url)).await()
    }

    suspend fun getPendingOrders(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/orders/pending")
        ).await()
    }

    suspend fun getMyOrders(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/orders/my-orders")
        ).await()
    }

    suspend fun getOrderDetail(id: Int): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/orders/$id")
        ).await()
    }

    suspend fun confirmPayment(id: Int): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/orders/$id/pay")
        ).await()
    }

    suspend fun cancelOrder(id: Int, trackingToken: String? = null): Response {
        val headers = if (trackingToken != null) {
            Headers.Builder().add("X-Tracking-Token", trackingToken).build()
        } else {
            Headers.Builder().build()
        }
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/orders/$id/cancel", headers = headers)
        ).await()
    }

    suspend fun updateOrderStatus(id: Int, request: UpdateOrderStatusRequest): Response {
        return networkHelper.client.newCall(
            PATCH(url = "$baseUrl/orders/$id/status", body = jsonBody(request))
        ).await()
    }

    // ── Points ──
    suspend fun getPointBalance(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/points/balance")
        ).await()
    }

    suspend fun getPointHistory(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/points/history")
        ).await()
    }

    // ── Offers ──
    suspend fun getOffers(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/offers")
        ).await()
    }

    suspend fun redeemOffer(request: RedeemOfferRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/offers/redeem", body = jsonBody(request))
        ).await()
    }

    suspend fun getUserOffers(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/user-offers")
        ).await()
    }

    suspend fun applyOffer(orderId: Int, request: ApplyOfferRequest): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/orders/$orderId/apply-offer", body = jsonBody(request))
        ).await()
    }

    // ── Analytics ──
    suspend fun getSales(from: String? = null, to: String? = null): Response {
        val url = buildString {
            append("$baseUrl/analytics/sales")
            val params = mutableListOf<String>()
            from?.let { params.add("from=$it") }
            to?.let { params.add("to=$it") }
            if (params.isNotEmpty()) {
                append("?${params.joinToString("&")}")
            }
        }
        return networkHelper.client.newCall(GET(url = url)).await()
    }

    suspend fun getTopItems(limit: Int? = null): Response {
        val url = if (limit != null) "$baseUrl/analytics/top-items?limit=$limit" else "$baseUrl/analytics/top-items"
        return networkHelper.client.newCall(GET(url = url)).await()
    }

    suspend fun getDailySummary(): Response {
        return networkHelper.client.newCall(
            GET(url = "$baseUrl/analytics/daily-summary")
        ).await()
    }

    // ── Warehouse ──
    suspend fun syncWarehouse(): Response {
        return networkHelper.client.newCall(
            POST(url = "$baseUrl/warehouse/sync")
        ).await()
    }
}
