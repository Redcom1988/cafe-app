package com.redcom1988.core.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTime(isoString: String?): String? {
    if (isoString == null) return null
    return try {
        val dt = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
        val local = dt.atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
        local.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")))
    } catch (_: Exception) {
        isoString
    }
}
