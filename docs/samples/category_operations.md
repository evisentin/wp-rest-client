# WordPress REST API Client for Java - Samples

## Creating a Category

Create a new WordPress category using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpCategoryCreateUpdateRequest` using the fluent builder API.
- Set common category attributes such as the name, slug, description, and parent category.
- Create the category and receive the resulting `WpCategory` instance returned by the WordPress REST API.
- Access the ID of the newly created category for further processing.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/CreateCategory.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/CreateCategory.java"
```

---

## Retrieving a Category

Retrieve a single WordPress category by its identifier using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve a category by its ID.
- Specify the desired `WpContext` (`VIEW`, `EDIT`, or `EMBED`) to control the information returned by the API.
- Receive the resulting `WpCategory` instance returned by the WordPress REST API.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent category.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/GetCategory.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/GetCategory.java"
```

---

## Deleting a Category

Delete a WordPress category using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Permanently delete a category and receive a `WpCategoryDeletionResponse`.
- Inspect the deletion result and determine whether the category was successfully deleted.
- Access summary information about the deleted category, such as its ID, name, slug, description, taxonomy, and link.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent category.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/DeleteCategory.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/DeleteCategory.java"
```

---

## Updating a Category

Update an existing WordPress category using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Build a `WpCategoryCreateUpdateRequest` using the fluent builder API.
- Perform a partial update by specifying only the category attributes that should change.
- Update an existing category by its ID and receive the resulting `WpCategory` instance returned by the WordPress REST
  API.
- Change common category attributes such as the name, slug, description, and parent category while leaving all
  unspecified fields unchanged.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent category.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/UpdateCategory.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/UpdateCategory.java"
```

---

## Listing Categories

Retrieve a paginated list of WordPress categories using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Filter categories using one or more query parameters, such as the associated post.
- Request a specific page of results.
- Read pagination metadata such as the total number of items and whether additional pages are available.
- Iterate over the returned categories and access common fields such as the ID, name, slug, and description.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/ListCategories.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/categories/ListCategories.java"
```
