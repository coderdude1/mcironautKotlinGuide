This documents several experiments using micronaut, kotlin and various ways to understand the way thread pools work in
micronaut/netty.

# Experiments

Initial setup

1. add kotlin coroutines to gradle file
2. created the async class with a controller, pojos, and several endpoints to experiment.
3. log statements upon entry to the method and after the sleep/pause to list the thrad number
4. According to the micronaut docs, the
   kotlin [kotlin coroutine support](https://docs.micronaut.io/latest/guide/#coroutines) just adding a suspend, with
   a `delay` should cause it pass work to the worker thread.

## GET on a suspend endpoint with a `Thread.sleep()`

One of the examples showed adding a delay using `Thread.sleep()`  would force a suspend function to offload the work to
a worker thread. According to the logs this didn't happen. I bumped the sleep to 10_000 and no luck getting a worker
thread (all `default-nioEventLoopGroup-1-2`)

    [default-nioEventLoopGroup-1-2] INFO com.dood.async.AsyncController - GET suspend blocking called
    [default-nioEventLoopGroup-1-2] INFO com.dood.async.AsyncController - after delay
    [default-nioEventLoopGroup-1-2] INFO my-access-logger - kubernetes.docker.internal - - [17/May/2022:22:06:35 -0500] "GET /async/nonblockingdelayed HTTP/1.1" 200 50

One of many articles I read talked about some schedulers looked at cpu and/or io loading before passing the work to a
worker thread. Since I just did the sleep, I will try adding a simple load and see what happens.

## kotlinx.coroutines.delay

Switching to the coroutines `delay` did work tho!  Initially before I imported the coroutines lib intellij suggested a
Mono.delay which took a duration. I did not try that.

    [default-nioEventLoopGroup-1-2] INFO com.dood.async.AsyncController - GET suspend blocking called
    [DefaultDispatcher-worker-1] INFO com.dood.async.AsyncController - after delay
    [default-nioEventLoopGroup-1-2] INFO my-access-logger - kubernetes.docker.internal - - [18/May/2022:16:54:54 -0500] "GET /async/nonblockingdelayed HTTP/1.1" 200 50

# General Observations

1. I invoked a non-blocking endpoint with a blocking call. This worked due to java NIO, ie the connection was made to an
   NIO channel which held the connection while the worker thread was
   active.  [This article on Java NIO](https://jenkov.com/tutorials/java-nio/non-blocking-server.html) was really
   helpful.
2. There are several ways to cause non-blocking
   io.  https://codingwithmohit.com/micronaut/micronaut-with-kotlin-coroutines/

# Stuff to look into and outstanding questions

2.

# kotlin suspend

I think every function in the call chain must be `suspend` or it may not work - figure out

# Links and such

This [article](https://medium.com/ing-blog/how-does-non-blocking-io-work-under-the-hood-6299d2953c74)  indicates that
there are time based and cpu based options that may cause work going to a worker thread, unblocking the request thread

[this article](https://codingwithmohit.com/micronaut/micronaut-with-kotlin-coroutines/) shows several ways to write the
controller to be non-blocking