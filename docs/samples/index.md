# WordPress REST API Client for Java - Samples

This section contains practical examples demonstrating how to use the WordPress REST API Client for Java. Each sample
focuses on a specific operation and showcases the recommended way to configure the client, authenticate with WordPress,
and interact with the REST API.

Whether you're just getting started or looking for a particular feature, these examples are intended to be copied,
adapted, and used as a starting point for your own applications.

**All the examples shown in this section are available in
the [`samples`](https://github.com/evisentin/wp-rest-client/tree/main/samples)
directory of the project repository. You can browse the complete source code, run the samples locally, or use them as a
starting point for your own applications.**

---

## Support class

The following utility class is shared by all the samples in this section. It creates a fully configured `WpRestClient`,
allowing you to choose the desired HTTP client implementation (Apache HttpClient or OkHttp) and authentication
mechanism (Basic Authentication or JWT Authentication). It also registers sample request and response interceptors to
demonstrate how interceptors can be added to the client.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/SampleClientBuilder.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/SampleClientBuilder.java"
```
