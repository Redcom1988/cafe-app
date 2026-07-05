package com.redcom1988.domain.table.repository

import com.redcom1988.domain.table.model.CafeTable
interface TableRepository {
    suspend fun getTableByQrCode(qrCode: String): CafeTable
}
