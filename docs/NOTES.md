# Notes

1. The Application class looks different when using the micronaut builder (in intellij) vs the example code in the
   quickstart
2. Enable annotation processing allows using the intellij builder vs gradle (gradle settings)
3. I **Think* I can use a mono with a publisher to hit a non-reactive endpoint
4. JSON serialization. I imported some jackson stuff, not sure if it is
   needed https://micronaut.io/2022/01/31/micronaut-serialization/#:~:text=Today%2C%20Micronaut%20co%2Dfounder%2C,other%20formats%20without%20using%20reflection
   .

# Current issues

## Serialization Issues

with the tests setup (functionalSpec) with 2 tests, and the endpoint returning a pojo, the tests run singly pass.
running them all they fail due to

    Caused by: io.micronaut.core.beans.exceptions.IntrospectionException: No serializable introspection present for type HelloResponse. Consider adding Serdeable. Serializable annotate to type HelloResponse. Alternatively if you are not in control of the project's source code, you can use @SerdeImport(HelloResponse.class) to enable serialization of this type.

and https://micronaut-projects.github.io/micronaut-serialization/snapshot/guide/#quickStart

Switching to `@Serdable` on the HelloResponse seems to work? I ***Think*** that this is needed for jackson and micronaut
serialization.

Not sure why the response properly serializes with each test by itself and fails without. I added the @JsonCreator per
the docs, thinking I need to do something else

# Links and such

## micronaut related

### Test

https://micronaut-projects.github.io/micronaut-test/snapshot/guide/index.html#kotest

# Concepts and stuff

Got this from several sites, including a SO post
https://stackoverflow.com/questions/57113781/how-does-nettys-non-blocking-threading-model-work
> The goal of asynchronous (non-blocking and event-driven) programming is to save core memory, when tasks are used
> instead of threads as units of parallel work. This allows to have millions of parallel activities instead of thousands

## Async, Blocking/NonBlocking IO, and Reactive

In my mind these things tend to be used interchangbly, and they really are not, or not as much as they are used.

## Blocking and nonBlocking IO

This is starting out as a list of questions I currently have. They may not be important but for now, I am curious. these
tend to be mostly around HTTP webservices and non-blocking

It starts with an external call to a service that is using a 'non-blocking' form of IO.
Most of the articles I read on this were light on some details and didn't answer questions I had, esp with the context
of an http api. Like an external caller requests an endpoint on a service with non-blocking io. Things like netty and
nodejs use a single request thread to handle requests, and can pass the processing of each request to worker threads (my
term). If that worker thread is busy doing IO (db, maybe calling other servvices, etc) what happens to the calling
connecftion? Is the connection paused, or is it passed to the worker thread?

Another thing I read about is that a worker thread can work on multiple requests (tasks in a netty article). how is this
switching performed? is it something like kotlin coroutines?

the frameworks I've worked with (ratpack, springboot, micronaut) all use netty (which uses NIO for the most part, at the
channel level). They seem to provide different ways to abstract that (promises in ratpack which ATM seems really
seamless, micronaut has several annotations to put blocking code in (@Blocking))

https://blog.codecentric.de/en/2019/04/explain-non-blocking-i-o-like-im-five/
https://thetechsolo.wordpress.com/2016/02/29/scalable-io-events-vs-multithreading-based/

this seems good
https://medium.com/ing-blog/how-does-non-blocking-io-work-under-the-hood-6299d2953c74

## IO and Micronaut

Request comes in and is handled by the event loop.  (1 or more threads?)

___Cleanup notes befoe commiting