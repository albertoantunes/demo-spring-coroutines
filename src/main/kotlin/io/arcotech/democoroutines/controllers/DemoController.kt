package io.arcotech.democoroutines.controllers

import io.arcotech.democoroutines.client.SomeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.core.publisher.Mono
import java.util.concurrent.Executors
import kotlin.concurrent.thread

@RestController
class DemoController {
    val customDispatcher = Executors.newFixedThreadPool(1000).asCoroutineDispatcher()

    private val webClient = WebClient.create("http://localhost:3000")

    @GetMapping("/coroutine")
    suspend fun coroutine(): ResponseEntity<SomeEntity> {
        val entity = webClient.get().awaitExchange { res -> res.awaitEntity<SomeEntity>() }
        println("Thread: ${Thread.currentThread().name}")
        return entity
    }

    @GetMapping("/blocking-wrapped")
    suspend fun blockingWrapped(): ResponseEntity<SomeEntity> {
        return withContext(customDispatcher) {
            println("Blocking Wrapped: ${Thread.currentThread().name}")
            Thread.sleep(1000)
            val someEntity = SomeEntity(1, "test")
            ResponseEntity.ok(someEntity)
        }
    }

    @GetMapping("/blocking")
    fun blocking(): ResponseEntity<SomeEntity> {
        println("Blocking: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        val someEntity = SomeEntity(1, "test")
        return ResponseEntity.ok(someEntity)
    }

    @GetMapping("/health")
    fun health(): String {
        println("Health: ${Thread.currentThread().name}")
        return "up"
    }

}