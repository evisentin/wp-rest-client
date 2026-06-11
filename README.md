# WP REST Client

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen)](LICENSE)

[//]: # ([![Coverage]&#40;https://img.shields.io/badge/coverage-report-brightgreen&#41;]&#40;https://evisentin.github.io/wp-rest-client/coverage/&#41;)


A type-safe Java client for the WordPress REST API with interchangeable HTTP client implementations.

## Features

- Type-safe Java API
- Automatic WordPress REST API discovery
- Apache HttpClient implementation
- OkHttp implementation
- Integration-tested against multiple WordPress versions
- Modular architecture

## Installation

### Apache HttpClient

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-apache</artifactId>
    <version>::latest::</version>
</dependency>
```

### OkHttp

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-okhttp</artifactId>
    <version>::latest::</version>
</dependency>
```

## Quick Example

```java
final WpRestClient client =
        ApacheWpRestClientBuilder.basicAuthentication(
                "https://my-wordpress-site.com",
                "admin",
                "password"
        ).build();

final List<WpPost> posts =
        client.posts()
                .list(WpPagingQuery.of(1, 10), WpPostQuery.builder().build())
                .getItems();
```

## Documentation

Full documentation is available at:

https://evisentin.github.io/wp-rest-client/

Topics covered include:

- Installation
- Authentication
- Getting Started
- Supported APIs
- Architecture
- Contributing
- Developer Setup

## Compatibility

WP REST Client is continuously tested against WordPress versions 6.4 through 7.0.

## License

Licensed under the Apache License 2.0.
