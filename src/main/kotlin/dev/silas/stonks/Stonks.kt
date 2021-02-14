package dev.silas.stonks

import org.springframework.data.annotation.Id
import java.time.ZonedDateTime

data class Stonks(
    @Id val id: Number? = null,
    val symbol: String,
    val value: Float,
    val timestamp: ZonedDateTime = ZonedDateTime.now()
)