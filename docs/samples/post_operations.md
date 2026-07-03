# WordPress REST API Client for Java - Samples - Post Operations

## Creating a Post

Create and publish a new WordPress post using the REST API.

This sample demonstrates how to:

- Configure a WpRestClient using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a WpPostCreateUpdateRequest using the fluent builder API.
- Set common post attributes such as the title, content, status, author, slug, excerpt, comment status, and ping status.
- Create the post and receive the resulting WpPost instance returned by the WordPress REST API.
- Access the ID of the newly created post for further processing.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/CreatePost.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/CreatePost.java"
```

---

## Retrieving a Post

Retrieve a single WordPress post by its identifier using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve a post by its ID.
- Specify the desired `WpContext` (`VIEW`, `EDIT`, or `EMBED`) to control the information returned by the API.
- Retrieve a password-protected post by supplying its password.
- Receive the resulting `WpPost` instance returned by the WordPress REST API.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent post.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/GetPost.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/GetPost.java"
```

---

## Deleting a Post

Delete a WordPress post using the REST API, either by moving it to the trash or by permanently removing it.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Move a post to the WordPress trash, allowing it to be restored later.
- Permanently delete a post and receive a `WpPostDeletionResponse`.
- Inspect the deletion result and determine whether the post was successfully deleted.
- Access summary information about the deleted post, such as its ID, title, slug, status, and content.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent post.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/DeletePost.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/DeletePost.java"
```

---

## Updating a Post

Update an existing WordPress post using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpPostCreateUpdateRequest` using the fluent builder API.
- Perform a partial update by specifying only the post attributes that should change.
- Update an existing post by its ID and receive the resulting `WpPost` instance returned by the WordPress REST API.
- Change common post attributes such as the title and publication status while leaving all unspecified fields unchanged.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent post.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/UpdatePost.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/UpdatePost.java"
```

---

## Listing Posts

Retrieve a paginated list of WordPress posts using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Filter posts by status (`Draft`, `Pending`, and `Published`).
- Request a specific page of results.
- Read pagination metadata such as the total number of items and whether additional pages are available.
- Iterate over the returned posts and access common fields such as the ID, title, slug, and status.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/ListPosts.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/posts/ListPosts.java"
```
