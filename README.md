# WP-REST-CLIENT

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![Javadocs](https://javadoc.io/badge2/io.github.evisentin/wp-rest-client/javadoc.svg)](https://javadoc.io/doc/io.github.evisentin/wp-rest-client)
[![Coverage Report](https://img.shields.io/badge/coverage-report-blue)](https://evisentin.github.io/wp-rest-client/coverage/)
[![License](https://img.shields.io/github/license/evisentin/wp-rest-client)](https://github.com/evisentin/wp-rest-client/blob/main/LICENSE)

![Java](https://img.shields.io/badge/Java-21-green)
![Java](https://img.shields.io/badge/Java-25-red)

<!-- toc -->

- [Overview](#overview)
- [Installation](#installation)
  * [Domain API](#domain-api)
  * [OkHttp implementation](#okhttp-implementation)
  * [Apache HttpClient implementation](#apache-httpclient-implementation)
- [Modules](#modules)
- [Supported REST APIs](#supported-rest-apis)
- [Usage](#usage)
- [Documentation](#documentation)
- [Maven Central](#maven-central)
- [License](#license)

<!-- tocstop -->

A modular Java client for interacting with the WordPress REST API.

## Overview

WP REST Client provides a type-safe Java API for WordPress REST endpoints, with interchangeable HTTP client
implementations.

Currently available implementations:

- OkHttp
- Apache HttpClient

## Installation

Artifacts are available on Maven Central.

### Domain API

Use this module if you only want to depend on the public API and abstractions.

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-domain</artifactId>
    <version>1.0.0</version>
</dependency>
```

### OkHttp implementation

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-okhttp</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Apache HttpClient implementation

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-apache</artifactId>
    <version>1.0.0</version>
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

> TODO: Usage guide and examples will be added in a future release.

## Documentation

- [Architecture](doc/architecture.md)
- [Contributing](doc/contributing.md)
- [Developer](doc/developer.md)

## Maven Central

- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-domain
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-apache
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-okhttp

## License

This project is licensed under the Apache License 2.0.

See the [LICENSE](LICENSE) file for details.
