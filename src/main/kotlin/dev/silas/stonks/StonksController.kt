package dev.silas.stonks

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class StonksController(
    val stonks: Flux<Stonks>
) {

    @GetMapping(
        "/stonks", produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun getAllStream(): Flux<Stonks> =
        stonks.onBackpressureDrop()

    @GetMapping(
        "/stonks/{symbol}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun getAllStreamBySymbol(
        @PathVariable symbol: String
    ): Flux<Stonks> = stonks.filter { it.symbol == symbol.toUpperCase() }
        .onBackpressureDrop()
}