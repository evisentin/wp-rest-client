# WP REST Client

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen)](https://github.com/evisentin/wp-rest-client/blob/main/LICENSE)
![Java](https://img.shields.io/badge/Java-21-informational)
![Java](https://img.shields.io/badge/Java-25-informational)

[//]: # ([![Coverage]&#40;https://img.shields.io/badge/coverage-report-brightgreen&#41;]&#40;coverage/&#41;)

A modular Java client for interacting with the WordPress REST API.

## Overview

WP REST Client provides a type-safe Java API for WordPress REST endpoints, with interchangeable HTTP client
implementations.

The client uses the WordPress REST
API [discovery mechanism](https://developer.wordpress.org/rest-api/using-the-rest-api/discovery/) to automatically
resolve the REST API root URL from the site base URL. Users typically only need to provide the WordPress site URL; the
client discovers the API endpoint according to the WordPress REST API discovery specification.

## Features

- Type-safe Java API for WordPress REST resources
- Pluggable HTTP client implementations
- Apache HttpClient 5 support
- OkHttp support
- WordPress REST API endpoint discovery
- Basic Authentication support
- JWT Authentication support
- Integration-tested WordPress compatibility

## Available Implementations

| Implementation      | Artifact                |
|---------------------|-------------------------|
| Apache HttpClient 5 | `wp-rest-client-apache` |
| OkHttp              | `wp-rest-client-okhttp` |

## Documentation

Start with the [Installation](getting_started/installation.md) page, then continue with [Quick Start](getting_started/quick-start.md).

Useful references:

- [Supported APIs](reference/supported-apis.md)
- [Architecture](development/architecture.md)
- [Contributing](development/contributing.md)
- [Developer Setup](development/developer-setup.md)

## Javadoc

- <https://javadoc.io/doc/io.github.evisentin/wp-rest-client>
- <https://javadoc.io/doc/io.github.evisentin/wp-rest-client-domain>
- <https://javadoc.io/doc/io.github.evisentin/wp-rest-client-apache>
- <https://javadoc.io/doc/io.github.evisentin/wp-rest-client-okhttp>

