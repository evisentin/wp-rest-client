# Authentication

WP REST Client currently provides builder methods for Basic Authentication and JWT Authentication.

Examples are available for both Apache HttpClient and OkHttp implementations.

## Apache HttpClient

### Basic Authentication

```java
import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;

final WpRestClient restClient =
        ApacheWpRestClientBuilder.basicAuthentication(
                "http://localhost:8080",
                "admin",
                "admin"
        ).build();
```

### JWT Authentication

```java
import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;

final WpRestClient restClient =
        ApacheWpRestClientBuilder.jwtAuthentication(
                "http://localhost:8080",
                "admin",
                "admin",
                "/api/v1/token"
        ).build();
```

The JWT token endpoint is relative to the discovered API URL.

## OkHttp

### Basic Authentication

```java
import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;

final WpRestClient restClient =
        OkHttpWpRestClientBuilder.basicAuthentication(
                "http://localhost:8080",
                "admin",
                "admin"
        ).build();
```

### JWT Authentication

```java
import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;

final WpRestClient restClient =
        OkHttpWpRestClientBuilder.jwtAuthentication(
                "http://localhost:8080",
                "admin",
                "admin",
                "/api/v1/token"
        ).build();
```

The JWT token endpoint is relative to the discovered API URL.

## Security Notes

Do not hard-code production credentials in source code. Prefer environment variables, secret managers, or your application framework's configuration mechanism.
