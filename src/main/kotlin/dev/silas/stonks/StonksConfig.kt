package dev.silas.stonks

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import javax.annotation.PreDestroy

@Configuration
class StonksConfig(
    private val stonksRepository: StonksRepository,
) {

    lateinit var disposable: Disposable

    @Bean
    fun updates(pricesProducer: Flux<Pair<String, Float>>): Flux<Stonks> =
        Flux.interval(Duration.ofSeconds(15)).flatMap {
            when (it) {
                0L -> stonksRepository.findAll()
                else -> stonksRepository.saveAll(
                    pricesProducer.map { Stonks(symbol = it.first, value = it.second) }
                )
            }
        }.replay()
            .autoConnect()

    @Bean
    fun startSubscribe(flux: Flux<Stonks>): Disposable =
        flux.subscribeOn(Schedulers.immediate())
            .subscribe().also {
                this.disposable = it
            }

    @PreDestroy
    fun shutDown() {
        disposable.dispose()
    }

    @Bean
    fun bitPandaClient(webClientBuilder: WebClient.Builder) =
        webClientBuilder
            .baseUrl("https://api.bitpanda.com")
            .build()

    @Bean
    fun pricesProducer(bitPandaClient: WebClient): Flux<Pair<String, Float>> =
        bitPandaClient
            .get()
            .uri("/v1/ticker")
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono {
                it.bodyToMono<Map<String, Map<String, Float>>>()
            }
            .flatMapIterable { prices ->
                prices.map { it.key to it.value.getOrDefault("EUR", -1f) }
            }
}