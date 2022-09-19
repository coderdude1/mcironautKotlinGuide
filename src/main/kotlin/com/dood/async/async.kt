package com.dood.async

import io.micronaut.core.annotation.Blocking
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.time.Instant

/**
 * This is an experiment with non-reactive, non-blocking io, aka async.  This seems to
 * be similar to Ratpack Promise chains
 *
 * No tests for now, I have been using the httpclients/async.http to call
 * and verify if things were done async (via the thread names) or not in the
 * server log outputs
 */
@Controller("/async")
class AsyncController(val asyncService: AsyncService) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val fibonacciLimit = 1000

    @Get(
        "/blockingnodelay",
        produces = [MediaType.APPLICATION_JSON]
    )
    fun noDelay(): AsyncResponse {
        logger.info("GET blocking called")
        val result = asyncService.fibonacciFold(fibonacciLimit)
        logger.info("after fibonacci limit $fibonacciLimit result: $result")
        return AsyncResponse("GET no delay processed by worker thread", Instant.now())
    }

    @Blocking //this only works if we have `AUTO` thread selection which by default is false.  use @ExecuteOn instead
    @Get("/blocking")
    fun blocking(): AsyncResponse {
        logger.info("@Blocking endpoing called")
        val result = asyncService.fibonacciFold(fibonacciLimit)
        logger.info("after fibonacci limit $fibonacciLimit result: $result")
        return AsyncResponse("GET to a @Blocking endpoint", Instant.now())
    }

    @ExecuteOn(TaskExecutors.IO) //pass work to a thread in the IO pool
    @Get(
        "executeon",
        produces = [MediaType.APPLICATION_JSON]
    )
    fun executeOn(): AsyncResponse {
        logger.info("GET on @ExeceuteOn endpoint")
        val result = asyncService.fibonacciFold(fibonacciLimit)
        logger.info("after fibonacci limit $fibonacciLimit result: $result")
        return AsyncResponse("@ExecuteOn endpoint called", Instant.now())
    }

    @Get(
        "nonblocking",
        produces = [MediaType.APPLICATION_JSON]
    )

    suspend fun nonblocking(): AsyncResponse {
        logger.info("GET suspend blocking called but call is handled by event loop not worker thread")
        return AsyncResponse("GET nonblocking but not threaded off", Instant.now())
    }

    @Get(
        "nonblockingdelayed",
        produces = [MediaType.APPLICATION_JSON]
    )
    suspend fun nonblockingDelayed(): AsyncResponse { //could not get this to work on another thread
        logger.info("GET suspend blocking called")
        //  latency(10_000)
        delay(1)
        logger.info("after delay")
        return AsyncResponse("GET nonblocking", Instant.now())
    }

    @Get(
        "nonblockingsleep",
        produces = [MediaType.APPLICATION_JSON]
    )
    suspend fun nonblockingSleep(): AsyncResponse {
        logger.info("GET suspend blocking called with thread.sleep")
        asyncService.latency(2_000)
        logger.info("after thread sleep")
        return AsyncResponse("GET nonblocking", Instant.now())
    }
}

@Introspected
@Serdeable
data class AsyncResponse(val name: String, val created: Instant)

@Singleton
class AsyncService {

    suspend fun latency(millis: Long) {
        //add a loop around this block
        println("latency")
        try {
            fibonacciFold(1000)
            Thread.sleep(millis) //not enough by itself to cause it to go to a working thread
        } catch (ignored: InterruptedException) {
        }
    }

    /**
     * Used to produce cpu load and a delay which may cause a response
     * to be passed to a worker thread.  maybe
     *  https://kousenit.org/2019/11/26/fibonacci-in-kotlin/
     */
    fun fibonacciFold(n: Int) =
        (2 until n).fold(1 to 1) { (prev, curr), _ ->
            curr to (prev + curr)
        }.second
}