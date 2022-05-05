package com.dood.hello

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import reactor.core.publisher.Mono

@MicronautTest
class HelloReactiveClientSpec(@Client("/") private val client: HelloClient) : FunSpec({
    test("happy path test") {
        val rsp = Mono.from(client.hello()).block()
        rsp shouldBe "Hello World"
    }
})