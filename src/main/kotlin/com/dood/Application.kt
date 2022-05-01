package com.dood

import io.micronaut.runtime.Micronaut.*
//This was created by the micronaut plugin and I selected 'kotlin'
fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.dood")
        .start()
}

/*
https://docs.micronaut.io/3.4.3/guide/index.html#quickStart
In the micronaut examples they show this for kotlin
object Application { //object is a singleton

    @JvmStatic //typically used for java interop
    fun main(args: Array<String>) {
        Micronaut.run(Application.javaClass)
    }
}
 */

