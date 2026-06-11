# Getting Started

This page shows how to create a WP REST Client instance and list WordPress posts.

## Endpoint Discovery

WP REST Client accepts a WordPress site URL rather than a REST API URL.

The client automatically discovers the REST API root using the standard WordPress REST API discovery mechanism and resolves all endpoint URLs from the discovered API index.

For example, provide the base site URL:

```text
http://localhost:8080
```

The client discovers the REST API endpoint automatically.

## List Posts with Apache HttpClient

Add the Apache implementation dependency:

```xml
<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-apache</artifactId>
    <version>::latest::</version>
</dependency>
```

Then create the client and list posts:

```java
import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;

import java.util.List;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;

final WpRestClient restClient =
        ApacheWpRestClientBuilder.basicAuthentication(
                "http://localhost:8080",
                "admin",
                "admin"
        ).build();

final WpPagingQuery pageQuery = WpPagingQuery.of(1, 10);
final WpPostQuery postQuery = WpPostQuery.builder()
        .withStatuses(Set.of(DRAFT, PUBLISH))
        .build();

final WpPagedResponse<WpPost> response = restClient.posts().list(pageQuery, postQuery);
final List<WpPost> posts = response.getItems();
```

## List Posts with OkHttp

Add the OkHttp implementation dependency:

```xml
<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-okhttp</artifactId>
    <version>::latest::</version>
</dependency>
```

Then create the client and list posts:

```java
import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;

import java.util.List;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;

final WpRestClient restClient =
        OkHttpWpRestClientBuilder.basicAuthentication(
                "http://localhost:8080",
                "admin",
                "admin"
        ).build();

final WpPagingQuery pageQuery = WpPagingQuery.of(1, 10);
final WpPostQuery postQuery = WpPostQuery.builder()
        .withStatuses(Set.of(DRAFT, PUBLISH))
        .build();

final WpPagedResponse<WpPost> response = restClient.posts().list(pageQuery, postQuery);
final List<WpPost> posts = response.getItems();
```

## Next Step

See [Authentication](authentication.md) for Basic Authentication and JWT Authentication examples.
