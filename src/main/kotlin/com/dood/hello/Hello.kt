package com.dood.hello

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/hello")
class HelloController {

    @Get(produces = [MediaType.TEXT_PLAIN]) //
    fun index(): String {
        return "Hello World" //
    }
}