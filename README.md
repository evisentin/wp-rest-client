# WordPress REST API Client for Java

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![Website](https://img.shields.io/badge/Website-Live-brightgreen)](https://evisentin.github.io/wp-rest-client/)
[![Javadoc](https://img.shields.io/badge/Javadoc-Online-green)](https://javadoc.io/doc/io.github.evisentin/wp-rest-client)
[![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen)](LICENSE)
[![Build](https://github.com/evisentin/wp-rest-client/actions/workflows/build.yml/badge.svg)](https://github.com/evisentin/wp-rest-client/actions/workflows/build.yml)
[![Coverage](https://img.shields.io/badge/coverage-report-brightgreen)](https://evisentin.github.io/wp-rest-client/coverage/)
![Java](https://img.shields.io/badge/Java-21-informational)
![Java](https://img.shields.io/badge/Java-25-informational)

wp-rest-client is an open-source WordPress REST API client for Java. It provides a modern, type-safe Java API for
interacting with the WordPress REST API, including posts, pages, media, categories, tags, users and custom endpoints.

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
                .list(WpPaginationQuery.of(1, 10), WpPostQuery.builder().build())
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

WP REST Client is continuously tested against WordPress versions 6.3 through 7.0.

## License

Licensed under the Apache License 2.0.
