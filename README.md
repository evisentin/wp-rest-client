# WP-REST-CLIENT

[![Maven Central](https://img.shields.io/maven-central/v/io.github.evisentin/wp-rest-client)](https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client)
[![Javadocs](https://javadoc.io/badge2/io.github.evisentin/wp-rest-client/javadoc.svg)](https://javadoc.io/doc/io.github.evisentin/wp-rest-client)
[![License](https://img.shields.io/github/license/evisentin/wp-rest-client)](https://github.com/evisentin/wp-rest-client/blob/main/LICENSE)

![Java](https://img.shields.io/badge/Java-21-green)
![Java](https://img.shields.io/badge/Java-25-red)
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
- [Supported REST APIs](#supported-rest-apis)
  * [Posts](#posts)
  * [Pages](#pages)
  * [Media](#media)
  * [Categories](#categories)
  * [Tags](#tags)
  * [Comments](#comments)
- [Usage](#usage)
- [Maven Central](#maven-central)
  * [Example](#example)
  * [Maven Central](#maven-central-1)
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
- `wp-rest-client-domain-assertions`  *(not deployed to Maven Central)*
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

## Supported REST APIs

This page documents the WordPress REST API endpoints supported by this client.

The complete WordPress REST API reference is available in the official WordPress
documentation: <https://developer.wordpress.org/rest-api/reference/>.

| Resource   |      Endpoint | Read | Create | Update | Delete | Notes                              |
|------------|--------------:|:----:|:------:|:------:|:------:|------------------------------------|
| Posts      |      `/posts` |  ✅   |   ✅    |   ✅    |   ✅    | Blog posts and post content.       |
| Pages      |      `/pages` |  ⬜   |   ⬜    |   ⬜    |   ⬜    | Static site pages.                 |
| Media      |      `/media` |  ⬜   |   ⬜    |   ⬜    |   ⬜    | Images, files, and attachments.    |
| Categories | `/categories` |  ✅   |   ✅    |   ✅    |   ✅    | Post categories.                   |
| Tags       |       `/tags` |  ✅   |   ✅    |   ✅    |   ✅    | Post tags.                         |
| Comments   |   `/comments` |  ⬜   |   ⬜    |   ⬜    |   ⬜    | Comments and moderation workflows. |
| Users      |      `/users` |  ⬜   |   ⬜    |   ⬜    |   ⬜    | Usually requires authentication.   |
| Search     |     `/search` |  ⬜   |  N/A   |  N/A   |  N/A   | Search across public content.      |
| Taxonomies | `/taxonomies` |  ⬜   |  N/A   |  N/A   |  N/A   | Taxonomy metadata.                 |
| Post Types |      `/types` |  ⬜   |  N/A   |  N/A   |  N/A   | Registered post type metadata.     |
| Statuses   |   `/statuses` |  ⬜   |  N/A   |  N/A   |  N/A   | Registered post statuses.          |
| Settings   |   `/settings` |  ⬜   |  N/A   |   ⬜    |  N/A   | Requires elevated permissions.     |

Legend:

- ✅ supported
- ⬜ not implemented yet
- N/A not applicable

### Posts

| Endpoint                   | Description     | Status |
|----------------------------|-----------------|--------|
| `GET /wp/v2/posts`         | List Posts      | ✅      |
| `POST /wp/v2/posts`        | Create a Post   | ✅      |
| `GET /wp/v2/posts/<id>`    | Retrieve a Post | ✅      |
| `POST /wp/v2/posts/<id>`   | Update a Post   | ✅      |
| `DELETE /wp/v2/posts/<id>` | Delete a Post   | ✅      |

### Pages

| Endpoint                   | Description     | Status |
|----------------------------|-----------------|--------|
| `GET /wp/v2/pages`         | List Pages      | ⬜      |
| `POST /wp/v2/pages`        | Create a Page   | ⬜      |
| `GET /wp/v2/pages/<id>`    | Retrieve a Page | ⬜      |
| `POST /wp/v2/pages/<id>`   | Update a Page   | ⬜      |
| `DELETE /wp/v2/pages/<id>` | Delete a Page   | ⬜      |

### Media

| Endpoint                   | Description           | Status |
|----------------------------|-----------------------|--------|
| `GET /wp/v2/media`         | List Media            | ⬜      |
| `POST /wp/v2/media`        | Create a Media item   | ⬜      |
| `GET /wp/v2/media/<id>`    | Retrieve a Media item | ⬜      |
| `POST /wp/v2/media/<id>`   | Update a Media item   | ⬜      |
| `DELETE /wp/v2/media/<id>` | Delete a Media  item  | ⬜      |

### Categories

| Endpoint                        | Description         | Status |
|---------------------------------|---------------------|--------|
| `GET /wp/v2/categories`         | List Categories     | ✅      |
| `POST /wp/v2/categories`        | Create a Category   | ✅      |
| `GET /wp/v2/categories/<id>`    | Retrieve a Category | ✅      |
| `POST /wp/v2/categories/<id>`   | Update a Category   | ✅      |
| `DELETE /wp/v2/categories/<id>` | Delete a Category   | ✅      |

### Tags

| Endpoint                  | Description    | Status |
|---------------------------|----------------|--------|
| `GET /wp/v2/tags`         | List Tags      | ✅      |
| `POST /wp/v2/tags`        | Create a Tag   | ✅      |
| `GET /wp/v2/tags/<id>`    | Retrieve a Tag | ✅      |
| `POST /wp/v2/tags/<id>`   | Update a Tag   | ✅      |
| `DELETE /wp/v2/tags/<id>` | Delete a Tag   | ✅      |

### Comments

| Endpoint                      | Description        | Status |
|-------------------------------|--------------------|--------|
| `GET /wp/v2/comments`         | List Comments      | ⬜      |
| `POST /wp/v2/comments`        | Create a Comment   | ⬜      |
| `GET /wp/v2/comments/<id>`    | Retrieve a Comment | ⬜      |
| `POST /wp/v2/comments/<id>`   | Update a Comment   | ⬜      |
| `DELETE /wp/v2/comments/<id>` | Delete a Comment   | ⬜      |

---

## Usage

> ⚠️ TODO: Usage guide and examples will be added in a future release.

---

## Maven Central

Artifacts are available on Maven Central:

- `io.github.evisentin:wp-rest-client-domain`
- `io.github.evisentin:wp-rest-client-apache`
- `io.github.evisentin:wp-rest-client-okhttp`

### Example

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-domain</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Maven Central

- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-domain
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-apache
- https://central.sonatype.com/artifact/io.github.evisentin/wp-rest-client-okhttp

---

## License

This project is licensed under the Apache License 2.0.

You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and limitations under the License.



