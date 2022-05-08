# Notes

1. The Application class looks different when using the micronaut builder (in intellij) vs the example code in the
   quickstart
2. Enable annotation processing allows using the intellij builder vs gradle (gradle settings)
3. I **Think* I can use a mono with a publisher to hit a non-reactive endpoint
4. JSON serialization. I imported some jackson stuff, not sure if it is
   needed https://micronaut.io/2022/01/31/micronaut-serialization/#:~:text=Today%2C%20Micronaut%20co%2Dfounder%2C,other%20formats%20without%20using%20reflection
   .

# Current issues

with the tests setup (functionalSpec) with 2 tests, and the endpoint returning a pojo, the tests run singly pass.
running them all they fail due to

    Caused by: io.micronaut.core.beans.exceptions.IntrospectionException: No serializable introspection present for type HelloResponse. Consider adding Serdeable. Serializable annotate to type HelloResponse. Alternatively if you are not in control of the project's source code, you can use @SerdeImport(HelloResponse.class) to enable serialization of this type.

and https://micronaut-projects.github.io/micronaut-serialization/snapshot/guide/#quickStart

Switching to `@Serdable` on the HelloResponse seems to work?

Not sure why the response properly serializes with each test by itself and fails without. I added the @JsonCreator per
the docs, thinking I need to do something else