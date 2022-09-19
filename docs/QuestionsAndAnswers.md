# Things I have questions on

## Event Loop Config

Micronaut uses netty which uses an event loop model.

1. How many threads in the event loop? Might just be one?

## Default Thread Pools

What are the default thread pools and their configurations for micronaut?

I've seen an IO pool mentioned. I've dabblec with some of the info endpoints with not a lot of
luck.  [here is an example of configuring the IO pool]((https://docs.micronaut.io/latest/guide/#reactiveServer)

### Thread Pools ive seen mentioned

In the config they show up under `microaut.executors.<threadpool name>`

1. IO
2. Scheduled