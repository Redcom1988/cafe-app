package com.redcom1988.domain.table.interactor

import com.redcom1988.domain.table.model.CafeTable
import com.redcom1988.domain.table.repository.TableRepository

class ScanTable(
    private val tableRepository: TableRepository
) {
    suspend fun await(qrCode: String): CafeTable {
        return tableRepository.getTableByQrCode(qrCode)
    }
}
