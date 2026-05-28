# WP-REST-CLIENT

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![Coverage Report](https://img.shields.io/badge/coverage-report-blue)](https://evisentin.github.io/wp-rest-client/coverage/)
[![License](https://img.shields.io/github/license/evisentin/wp-rest-client)](https://github.com/evisentin/wp-rest-client/blob/main/LICENSE)

![Java](https://img.shields.io/badge/Java-21-green)
![Java](https://img.shields.io/badge/Java-25-red)

<!-- toc -->

- [Overview](#overview)
- [Javadoc](#javadoc)
- [Maven Central](#maven-central)
- [Installation](#installation)
  * [Domain API](#domain-api)
  * [OkHttp implementation](#okhttp-implementation)
  * [Apache HttpClient implementation](#apache-httpclient-implementation)
- [Modules](#modules)
- [Supported REST APIs](#supported-rest-apis)
- [Usage](#usage)
  * [Sample with Apache HttpClient 5](#sample-with-apache-httpclient-5)
  * [Sample with OKHTTP](#sample-with-okhttp)
- [Documentation](#documentation)
- [License](#license)

<!-- tocstop -->

A modular Java client for interacting with the WordPress REST API.

## Overview

WP REST Client provides a type-safe Java API for WordPress REST endpoints, with interchangeable HTTP client
implementations.

Currently available implementations:

- [OkHttp](https://square.github.io/okhttp/)
- [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.6.x/index.html)

## Javadoc

- https://javadoc.io/doc/io.github.evisentin/wp-rest-client
- https://javadoc.io/doc/io.github.evisentin/wp-rest-client-domain
- https://javadoc.io/doc/io.github.evisentin/wp-rest-client-apache
- https://javadoc.io/doc/io.github.evisentin/wp-rest-client-okhttp

## Maven Central

- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-domain
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-apache
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-okhttp

## Installation

Artifacts are available on Maven Central.

### Domain API

Use this module if you only want to depend on the public API and abstractions.

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-domain</artifactId>
    <version>::latest::</version>
</dependency>
```

### OkHttp implementation

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-okhttp</artifactId>
    <version>::latest::</version>
</dependency>
```

### Apache HttpClient implementation

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-apache</artifactId>
    <version>::latest::</version>
</dependency>
```

## Modules

| Module                  | Purpose                                     |
|-------------------------|---------------------------------------------|
| `wp-rest-client-domain` | Public API, interfaces, DTOs, and contracts |
| `wp-rest-client-okhttp` | OkHttp-based implementation                 |
| `wp-rest-client-apache` | Apache HttpClient-based implementation      |

Internal testing and architecture modules are documented in [`docs/architecture.md`](doc/architecture.md).

## Supported REST APIs

See [Supported REST APIs](doc/api.md)

## Usage

### Sample with Apache HttpClient 5

Import the right module

```xml

<dependencies>
    <dependency>
        <groupId>io.github.evisentin</groupId>
        <artifactId>wp-rest-client-apache</artifactId>
        <version>::latest::</version>
    </dependency>
</dependencies>
```

List the posts

```java
import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;

import java.util.List;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;

...
// Initialize the client
final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("username", "password");
final ApacheWpRestClient restClient = new ApacheWpRestClient(
        "http://localhost:8080", // baseUrl
        authenticationStrategy,
        null, // ssl configuration
        null // timeout configuration
);

final WpPagingQuery pageQuery = WpPagingQuery.of(1, 10);
final WpPostQuery postQuery = WpPostQuery.builder()
        .withStatuses(Set.of(DRAFT, PUBLISH))
        .build();

final WpPagedResponse<WpPost> response = restClient.listPosts(pageQuery, postQuery);
final List<WpPost> posts = response.getItems();
...
```

### Sample with OKHTTP

Import the right module

```xml

<dependencies>
    <dependency>
        <groupId>io.github.evisentin</groupId>
        <artifactId>wp-rest-client-okhttp</artifactId>
        <version>::latest::</version>
    </dependency>
</dependencies>
```

List the posts

```java
import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;

import java.util.List;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;

...
// Initialize the client
final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("username", "password");
final OkHttpWpRestClient restClient = new OkHttpWpRestClient(
        "http://localhost:8080", // baseUrl
        authenticationStrategy,
        null, // ssl configuration
        null // timeout configuration
);

final WpPagingQuery pageQuery = WpPagingQuery.of(1, 10);
final WpPostQuery postQuery = WpPostQuery.builder()
        .withStatuses(Set.of(DRAFT, PUBLISH))
        .build();

final WpPagedResponse<WpPost> response = restClient.listPosts(pageQuery, postQuery);
final List<WpPost> posts = response.getItems();
...
```

## Documentation

- [Architecture](doc/architecture.md)
- [Contributing](doc/contributing.md)
- [Developer](doc/developer.md)

## License

This project is licensed under the Apache License 2.0.

See the [LICENSE](LICENSE) file for details.
