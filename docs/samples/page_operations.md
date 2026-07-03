# WordPress REST API Client for Java - Samples

## Creating a Page

Create and publish a new WordPress page using the REST API.

This sample demonstrates how to:

- Configure a WpRestClient using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a WpPageCreateUpdateRequest using the fluent builder API.
- Set common page attributes such as the title, content, status, author, slug, excerpt, comment status, and ping status.
- Create the page and receive the resulting WpPage instance returned by the WordPress REST API.
- Access the ID of the newly created page for further processing.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/CreatePage.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/CreatePage.java"
```

---

## Retrieving a Page

Retrieve a single WordPress page by its identifier using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve a page by its ID.
- Specify the desired `WpContext` (`VIEW`, `EDIT`, or `EMBED`) to control the information returned by the API.
- Retrieve a password-protected page by supplying its password.
- Receive the resulting `WpPage` instance returned by the WordPress REST API.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent page.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/GetPage.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/GetPage.java"
```

---

## Deleting a Page

Delete a WordPress page using the REST API, either by moving it to the trash or by permanently removing it.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Move a page to the WordPress trash, allowing it to be restored later.
- Permanently delete a page and receive a `WpPageDeletionResponse`.
- Inspect the deletion result and determine whether the page was successfully deleted.
- Access summary information about the deleted page, such as its ID, title, slug, status, and content.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent page.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/DeletePage.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/DeletePage.java"
```

---

## Updating a Page

Update an existing WordPress page using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpPageCreateUpdateRequest` using the fluent builder API.
- Perform a partial update by specifying only the page attributes that should change.
- Update an existing page by its ID and receive the resulting `WpPage` instance returned by the WordPress REST API.
- Change common page attributes such as the title and publication status while leaving all unspecified fields unchanged.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent page.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/UpdatePage.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/UpdatePage.java"
```

---

## Listing Pages

Retrieve a paginated list of WordPress pages using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Filter posts by status (`Draft`, `Pending`, and `Published`).
- Request a specific page of results.
- Read pagination metadata such as the total number of items and whether additional pages are available.
- Iterate over the returned pages and access common fields such as the ID, title, slug, and status.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/ListPages.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/pages/ListPages.java"
```
