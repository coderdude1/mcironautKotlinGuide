package com.dood.hello

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

@Client("/hello")
interface HelloClient {

    @Get(consumes = [MediaType.TEXT_PLAIN])//by default inherits the /hello.  can override
    @SingleResult
    fun hello(): Publisher<String> //reactive
}