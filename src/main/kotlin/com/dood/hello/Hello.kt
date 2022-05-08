package com.dood.hello

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Controller("/hello")
class HelloController {

    @Get(produces = [MediaType.APPLICATION_JSON]) //
    fun index(): HelloResponse {
        return HelloResponse("Hello World", Instant.now())//
    }
}

//TODO add validation to request
data class HelloRequest(val salutation: String, val name: String)

@Serdeable
data class HelloResponse(val response: String, val createTime: Instant)