# Contributing

This document describes how to contribute to WP REST Client.

---

## Adding a New Implementation

To add a new HTTP client implementation:

1. Create a new module, for example `wp-rest-client-xyz`
2. Implement the interfaces from `wp-rest-client-domain`
3. Reuse the shared contract tests from `wp-rest-client-contract-tests`
4. Provide implementation-specific test configuration
5. Ensure all tests pass

A new implementation should not define its own behavioural rules.

The expected behaviour is defined by the domain API and verified by the shared contract tests.

---

## Implementation Requirements

A compliant implementation should:

- Implement the public interfaces from `wp-rest-client-domain`
- Preserve the same behaviour as the existing implementations
- Map WordPress REST responses to the shared DTOs
- Handle errors consistently
- Pass the shared contract test suite

## Testing

Before opening a pull request, run the full test suite.

```shell
mvn clean verify -Plocal-tests,integration-tests
```

---

## Contract Tests

All implementations must pass the shared contract tests.

Contract tests are located in:

```text
wp-rest-client-contract-tests
```

These tests define the expected behaviour of any compliant implementation.

---

## Integration Tests

Integration tests are located in:

```text
wp-rest-client-test-integration
```

They verify compatibility against real WordPress instances using Testcontainers.

---

## Internal Modules

The following modules are used for development and testing only:

- `wp-rest-client-domain-assertions`
- `wp-rest-client-contract-tests`
- `wp-rest-client-test-integration`
- `wp-rest-client-test-report`

These modules are not intended for library consumers and are not deployed to Maven Central.

---

## Documentation

User-facing documentation belongs in the root `README.md`.

Design, testing, and contributor-focused documentation belongs in the `docs/` directory.
