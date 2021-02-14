package dev.silas.stonks

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface StonksRepository : ReactiveCrudRepository<Stonks, Number>