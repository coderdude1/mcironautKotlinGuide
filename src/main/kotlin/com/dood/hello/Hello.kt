package com.dood.hello

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.serde.annotation.Serdeable
import org.slf4j.LoggerFactory
import java.time.Instant

@Controller("/hello")
class HelloController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Get(produces = [MediaType.APPLICATION_JSON]) //
    fun index(): HelloResponse {
        return HelloResponse("Hello World", Instant.now())//
    }

    @Post(produces = [MediaType.APPLICATION_JSON])
    fun create(request: HelloRequest): HelloResponse {
        logger.info("received an input: $request")
        return HelloResponse("${request.salutation} ${request.name}!", Instant.now())
    }
}

//TODO add validation to request
@Introspected
@Serdeable //how to do jackson?
data class HelloRequest(val salutation: String, val name: String)

@Serdeable
data class HelloResponse(val response: String, val createTime: Instant)