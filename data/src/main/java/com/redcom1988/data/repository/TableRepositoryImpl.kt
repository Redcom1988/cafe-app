package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.table.TableResponse
import com.redcom1988.domain.table.model.CafeTable
import com.redcom1988.domain.table.repository.TableRepository

class TableRepositoryImpl(
    private val api: Cafej3Api
) : TableRepository {

    override suspend fun getTableByQrCode(qrCode: String): CafeTable {
        val response = api.getTableByQrCode(qrCode)
        if (!response.isSuccessful) throw Exception("Table not found (HTTP ${response.code})")
        return response.parseAs<TableResponse>().toDomain()
    }
}
