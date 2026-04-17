# WP-REST-CLIENT

----

<!-- toc -->

- [Overview](#overview)
- [Modules](#modules)
  * [Module `wp-rest-client-domain`](#module-wp-rest-client-domain)
  * [Module `wp-rest-client-apache`](#module-wp-rest-client-apache)
  * [Module `wp-rest-client-okhttp`](#module-wp-rest-client-okhttp)
  * [Module `wp-rest-client-test-support`](#module-wp-rest-client-test-support)
  * [Module `wp-rest-client-test-integration`](#module-wp-rest-client-test-integration)
  * [Module `wp-rest-client-test-report`](#module-wp-rest-client-test-report)
- [Key Concept: Contract Testing](#key-concept-contract-testing)
  * [Why this matters](#why-this-matters)
  * [How it works](#how-it-works)
- [Adding a New Implementation](#adding-a-new-implementation)
- [Design Goals](#design-goals)
- [Usage](#usage)
- [Maven (coming soon)](#maven-coming-soon)
- [License](#license)

<!-- tocstop -->

----

A modular, extensible Java client for interacting with the WordPress REST API.

This project provides a clean separation between API contracts, implementations, and testing, allowing multiple HTTP
client implementations to coexist while guaranteeing consistent behaviour through shared contract tests.

## Overview

The goal of this project is to provide:

- A **type-safe Java client** for the WordPress REST API
- Multiple interchangeable HTTP client implementations
- A **contract-driven architecture** to ensure consistency across implementations
- A clean and modular Maven structure

Currently, the project includes two implementations:

- **OkHttp-based client**
- **Apache HttpClient-based client**

## Modules

The project is organised as a Maven multi-module build:

- `wp-rest-client-domain`
- `wp-rest-client-apache`
- `wp-rest-client-okhttp`
- `wp-rest-client-test-support` *(not deployed to Maven Central)*
- `wp-rest-client-test-integration` *(not deployed to Maven Central)*
- `wp-rest-client-test-report` *(not deployed to Maven Central)*

### Module `wp-rest-client-domain`

Contains:

- Core interfaces
- DTOs (Data Transfer Objects)
- API contracts

This module defines the **public API** of the client and is implementation-agnostic.

**PLEASE NOTE:** If you only depend on abstractions, this is your entry point.

---

### Module `wp-rest-client-apache`

Provides an implementation based on:

- [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/index.html)

Responsibilities:

- HTTP communication using Apache HttpClient
- Same contract as other implementations

---

### Module `wp-rest-client-okhttp`

Provides an implementation based on:

- [OkHttp](https://square.github.io/okhttp/)

Responsibilities:

- HTTP communication using OkHttp
- Serialization/deserialization
- Mapping responses to domain DTOs


---

### Module `wp-rest-client-test-support`

This is one of the **most important modules** in the project.

Contains:

- Shared **contract tests**
- Test utilities
- Common test fixtures

**PLEASE NOTE:** This module defines the **expected behaviour** of any compliant implementation.

---

### Module `wp-rest-client-test-integration`

Contains:

- Integration tests based on **Testcontainers**
- Test suites executed against multiple WordPress versions

Purpose:

- Ensure **compatibility across different WordPress versions**

**PLEASE NOTE:** This module is **not deployed to Maven Central**.

---

### Module `wp-rest-client-test-report`

Contains:

- Aggregated test reports
- Cross-module test execution results

Useful for:

- Verifying consistency across implementations
- CI/CD integration

---

## Key Concept: Contract Testing

A central design principle of this project is **contract testing**.

Instead of duplicating tests for each implementation, we define a **shared test suite** that all implementations must
pass.

### Why this matters

- Guarantees **behavioural consistency**
- Prevents divergence between implementations
- Makes it easy to add new HTTP client implementations in the future

### How it works

1. Contract tests are defined in `wp-rest-client-test-support`
2. Each implementation module:
    - Reuses the same tests
    - Provides its own configuration
3. All implementations must pass the **same test suite**

**PLEASE NOTE:** If a new implementation passes the contract tests, it is considered compliant.

---

## Adding a New Implementation

To add a new HTTP client implementation:

1. Create a new module (e.g. `wp-rest-client-xyz`)
2. Implement the interfaces from `wp-rest-client-domain`
3. Reuse the contract tests from `wp-rest-client-test-support`
4. Ensure all tests pass

That’s it — no need to rewrite test logic.

---

## Design Goals

- Clean separation of concerns
- Pluggable HTTP layer
- Strongly typed API
- Test-driven consistency
- Easy extensibility

---

## Usage

> ⚠️ TODO: Usage guide and examples will be added in a future release.

---

## Maven (coming soon)

> ⚠️ TODO: Maven Central coordinates will be published soon.

---

## License

This project is licensed under the Apache License 2.0.

You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and limitations under the License.



