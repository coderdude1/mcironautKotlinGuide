package com.dood.hello

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.time.Instant

/**
 * General Notes
 * 1.  we need to use blockingIo otherwise the client methods return a publisher, which
 * requires a subscriber.  ie the `toBlocking()` returns a BlockingClient which does away
 * with the reactive stuff
 */
@MicronautTest
class HelloControllerSpec(@Client("/") private val client: HttpClient) : FunSpec({
    test("GET Simple HelloResponse") {
        val rsp: HelloResponse =
            client.toBlocking() //todo figure out how to set header accept json; how to serailize from json to pojo
                .retrieve(GET<Any>("/hello"), HelloResponse::class.java)
        rsp.response shouldBe "Hello World"
        rsp.createTime shouldBeBefore Instant.now()
    }
    test("POST /hello") {
        val request = HelloRequest("Hello", "dood")
        val rsp = client.toBlocking().exchange( //toblocking isnt prefered vs subscribe (reactive)
            POST("/hello", request), HelloResponse::class.java
        )
        rsp.status shouldBe HttpStatus.OK
        rsp.body() != null //let block?
        val body = rsp.body.get()
        //kotest and optionals https://www.novatec-gmbh.de/blog/kotlin-assertion-libraries-kotest-assertions/
        body.response.shouldBe("Hello dood!")
        body.createTime.shouldBeBefore(Instant.now())
    }

    test("POST /hello with a HttpClient retreive") {
        val request = HelloRequest("Hello", "dood")
        val rsp =
            client.toBlocking().retrieve(POST("/hello", request)) //toblocking isnt prefered vs subscribe (reactive)
        //retreive only givess us the body, not the headers and status and such
//response contains the JSON string of HelloResponse
        rsp.contains("Hello dood!")
    }
})

@MicronautTest
class HelloControllerBehavioralSpec(@Client("/") private val client:HttpClient) : BehaviorSpec({
    given("A helloworld controller") {
        `when`("a simple GET call") { //note single quote, when is a reserved kotlin word.  When is an option
            val rsp: HelloResponse = client.toBlocking()
                .retrieve(GET<Any>("/hello"), HelloResponse::class.java)

            then("check the response") {
                rsp.response shouldBe "Hello World"
                rsp.createTime shouldBeBefore Instant.now()
            }
        }
    }
})