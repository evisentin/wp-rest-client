# WordPress REST API Client for Java - Samples

## Retrieving a Taxonomy

Retrieve information about a single WordPress taxonomy using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve a taxonomy by its unique name.
- Receive the resulting `WpTaxonomyInfo` instance returned by the WordPress REST API.
- Access common taxonomy metadata such as the name, description, slug, supported object types, and whether the taxonomy
  is hierarchical.
- Handle common error scenarios such as unauthorized access, insufficient permissions, or a non-existent taxonomy.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/taxonomies/GetTaxonomy.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/taxonomies/GetTaxonomy.java"
```

---

## Listing Taxonomies

Retrieve all available WordPress taxonomies using the REST API.

This sample demonstrates how to:

- Configure a `WpRestClient` using either Apache HttpClient or OkHttp.
- Authenticate using either Basic Authentication or JWT Authentication.
- Retrieve all taxonomies available on the WordPress installation.
- Receive the resulting map keyed by taxonomy name.
- Access the `WpTaxonomyInfo` associated with each taxonomy to inspect its metadata.

```java title="samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/taxonomies/ListTaxonomies.java"
--8<-- "samples/src/main/java/io/github/evisentin/wordpress/rest/client/samples/code/taxonomies/ListTaxonomies.java"
```
