package io.arcotech.democoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoCoroutinesApplication

fun main(args: Array<String>) {
	runApplication<DemoCoroutinesApplication>(*args)
}
