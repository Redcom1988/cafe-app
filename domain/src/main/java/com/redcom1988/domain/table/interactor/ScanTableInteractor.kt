package com.redcom1988.domain.table.interactor

import com.redcom1988.domain.table.model.CafeTable
import com.redcom1988.domain.table.repository.TableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScanTable(
    private val tableRepository: TableRepository
) {
    suspend fun await(qrCode: String): CafeTable = withContext(Dispatchers.IO) {
        tableRepository.getTableByQrCode(qrCode)
    }
}
