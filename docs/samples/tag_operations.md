# WordPress REST API Client for Java - Samples

## Creating a Tag

Create a new WordPress tag using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpTagCreateUpdateRequest` using the fluent builder API.
- Set common tag attributes such as the name, slug, description, and parent tag.
- Create the tag and receive the resulting `WpTag` instance returned by the WordPress REST API.
- Access the ID of the newly created tag for further processing.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/CreateTag.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/CreateTag.java"
```

---

## Retrieving a Tag

Retrieve a single WordPress tag by its identifier using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve a tag by its ID.
- Specify the desired `WpContext` (`VIEW`, `EDIT`, or `EMBED`) to control the information returned by the API.
- Receive the resulting `WpTag` instance returned by the WordPress REST API.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent tag.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/GetTag.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/GetTag.java"
```

---

## Deleting a Tag

Delete a WordPress tag using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Permanently delete a tag and receive a `WpTagDeletionResponse`.
- Inspect the deletion result and determine whether the tag was successfully deleted.
- Access summary information about the deleted tag, such as its ID, name, slug, description, taxonomy, and link.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent tag.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/DeleteTag.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/DeleteTag.java"
```

---

## Updating a Tag

Update an existing WordPress tag using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpTagCreateUpdateRequest` using the fluent builder API.
- Perform a partial update by specifying only the tag attributes that should change.
- Update an existing tag by its ID and receive the resulting `WpTag` instance returned by the WordPress REST
  API.
- Change common tag attributes such as the name, slug, description, and parent tag while leaving all
  unspecified fields unchanged.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent tag.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/UpdateTag.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/UpdateTag.java"
```

---

## Listing Tags

Retrieve a paginated list of WordPress tags using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Filter tags using one or more query parameters, such as the associated post.
- Request a specific page of results.
- Read pagination metadata such as the total number of items and whether additional pages are available.
- Iterate over the returned tags and access common fields such as the ID, name, slug, and description.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/ListTags.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/tags/ListTags.java"
```
