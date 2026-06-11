# Installation

Artifacts are available on Maven Central.

WP REST Client is organized into a modular architecture:

- **Domain API**: public interfaces, models, DTOs, and abstractions.
- **HTTP client implementations**: concrete clients based on Apache HttpClient or OkHttp.

Most applications should depend on exactly one implementation module: either `wp-rest-client-apache` or
`wp-rest-client-okhttp`.

## Requirements

- Java 21 or Java 25
- Maven or another JVM build tool
- A WordPress site exposing the WordPress REST API

## Maven Dependencies

### Apache HttpClient Implementation

Use this dependency if your application already uses Apache HttpClient, or if you want to use the Apache-based
implementation.

=== "Maven"
    ```xml title="pom.xml"
    <dependency>
      <groupId>io.github.evisentin</groupId>
      <artifactId>wp-rest-client-apache</artifactId>
      <version>::latest::</version>
    </dependency>
    ```
=== "Gradle (groovy)"
    ```groovy title="build.gradle"
    dependencies {
        implementation 'io.github.evisentin:wp-rest-client-apache:<latest version>'
    }
    ```
=== "Gradle (kotlin)"
    ```kotlin title="build.gradle.kts"
    dependencies {
        implementation("io.github.evisentin:wp-rest-client-apache:<latest version>")
    }
    ```

### OkHttp Implementation

Use this dependency if your application already uses OkHttp, or if you prefer the OkHttp-based implementation.

=== "Maven"
    ```xml title="pom.xml"
    <dependency>
      <groupId>io.github.evisentin</groupId>
      <artifactId>wp-rest-client-okhttp</artifactId>
      <version>::latest::</version>
    </dependency>
    ```
=== "Gradle (groovy)"
    ```groovy title="build.gradle"
    dependencies {
        implementation 'io.github.evisentin:wp-rest-client-okhttp:<latest version>'
    }
    ```
=== "Gradle (kotlin)"
    ```kotlin title="build.gradle.kts"
    dependencies {
        implementation("io.github.evisentin:wp-rest-client-okhttp:<latest version>")
    }
    ```

### Domain API

Use this module only if you want to depend on the public API, DTOs, and abstractions without selecting a concrete HTTP
implementation.

=== "Maven"
    ```xml title="pom.xml"
    <dependency>
      <groupId>io.github.evisentin</groupId>
      <artifactId>wp-rest-client-domain</artifactId>
      <version>::latest::</version>
    </dependency>
    ```
=== "Gradle (groovy)"
    ```groovy title="build.gradle"
    dependencies {
        implementation 'io.github.evisentin:wp-rest-client-domain:<latest version>'
    }
    ```
=== "Gradle (kotlin)"
    ```kotlin title="build.gradle.kts"
    dependencies {
        implementation("io.github.evisentin:wp-rest-client-domain:<latest version>")
    }
    ```

## Which Module Should I Use?

| Scenario                        | Dependency              |
|---------------------------------|-------------------------|
| I want to use Apache HttpClient | `wp-rest-client-apache` |
| I want to use OkHttp            | `wp-rest-client-okhttp` |

## Next Step

After adding a dependency, continue with [Quick Start](quick-start.md) to create your first client and execute
requests against the WordPress REST API.
