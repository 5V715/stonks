package dev.silas.stonks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StonksApplication

fun main(args: Array<String>) {
    runApplication<StonksApplication>(*args)
}