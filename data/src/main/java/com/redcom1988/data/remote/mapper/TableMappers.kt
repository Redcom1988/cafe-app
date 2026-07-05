package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.table.TableResponse
import com.redcom1988.domain.table.model.CafeTable

fun TableResponse.toDomain(): CafeTable = CafeTable(
    id = tableId,
    tableNumber = tableNumber,
    qrCode = qrCode,
    status = status
)
