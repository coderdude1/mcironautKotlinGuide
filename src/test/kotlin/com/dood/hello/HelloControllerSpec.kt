package com.dood.hello

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class HelloControllerSpec(@Client("/") private val client: HttpClient) : FunSpec({
    test("Simple hello world response") {
        val rsp: String = client.toBlocking()
            .retrieve("/hello")
        rsp shouldBe "Hello World"
    }
})

@MicronautTest
class HelloControllerBehavioralSpec(@Client("/") private val client:HttpClient) : BehaviorSpec({
    given("A helloworld controller") {
        `when`("a simple GET call") { //note single quote, when is a reserved kotlin word.  When is an option
            val rsp: String = client.toBlocking()
                .retrieve("/hello")

            then("check the response") {
                rsp shouldBe "Hello World"
            }
        }
    }
})