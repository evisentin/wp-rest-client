# Architecture

This document describes the internal architecture and design principles of WP REST Client.

## Design Goals

The project is designed around the following goals:

- Clean separation of concerns
- Pluggable HTTP client layer
- Strongly typed API
- Implementation-agnostic domain model
- Behavioural consistency across implementations
- Easy extensibility for future HTTP clients

## Module Structure

The project is organised as a Maven multi-module build.

| Module                             | Description                                     |
|------------------------------------|-------------------------------------------------|
| `wp-rest-client-domain`            | Public API, DTOs, interfaces, and API contracts |
| `wp-rest-client-apache`            | Apache HttpClient implementation                |
| `wp-rest-client-okhttp`            | OkHttp implementation                           |
| `wp-rest-client-domain-assertions` | Test assertions for domain objects              |
| `wp-rest-client-contract-tests`    | Shared contract tests for implementations       |
| `wp-rest-client-test-integration`  | Integration tests using Testcontainers          |
| `wp-rest-client-test-report`       | Aggregated test reports                         |

The following modules are not deployed to Maven Central:

- `wp-rest-client-domain-assertions`
- `wp-rest-client-contract-tests`
- `wp-rest-client-test-integration`
- `wp-rest-client-test-report`

## Public API

The `wp-rest-client-domain` module defines the public API of the client.

It contains:

- Core interfaces
- DTOs
- API contracts

This module is implementation-agnostic and should not depend on a specific HTTP client.

## Implementations

Each implementation module provides the same behaviour using a different HTTP client.

Current implementations:

- `wp-rest-client-okhttp`
- `wp-rest-client-apache`

Each implementation is responsible for:

- HTTP communication
- Request construction
- Response handling
- Serialization and deserialization
- Mapping responses to domain DTOs

## Key Concept: Contract Testing

A central design principle of this project is contract testing.

Instead of duplicating tests for each implementation, the project defines a shared test suite that every implementation
must pass.

## Why Contract Testing Matters

Contract testing helps ensure that all implementations behave consistently.

It provides the following benefits:

- Guarantees behavioural consistency
- Prevents divergence between implementations
- Makes it easier to add new HTTP client implementations
- Reduces duplicated test logic
- Documents the expected behaviour of the public API

## How Contract Testing Works

Contract tests are defined in `wp-rest-client-contract-tests`.

Each implementation module:

1. Reuses the shared contract tests
2. Provides its own implementation-specific configuration
3. Runs the same behavioural test suite

If an implementation passes the contract tests, it is considered compliant.

## Integration Testing

The `wp-rest-client-test-integration` module contains integration tests based on Testcontainers.

These tests are used to verify compatibility with multiple WordPress versions.

## Test Reports

The `wp-rest-client-test-report` module contains aggregated test reports and cross-module execution results.

This is useful for:

- Verifying consistency across implementations
- Reviewing CI/CD results
- Tracking compatibility across supported WordPress versions
