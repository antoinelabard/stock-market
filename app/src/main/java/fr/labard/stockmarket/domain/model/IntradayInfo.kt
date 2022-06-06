package fr.labard.stockmarket.domain.model

import java.time.LocalDateTime

class IntradayInfo(
    val date: LocalDateTime,
    val close: Double
)