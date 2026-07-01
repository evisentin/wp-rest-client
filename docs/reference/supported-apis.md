# Supported WP REST APIs

This page documents the WordPress REST API endpoints supported by this client.

The complete WordPress REST API reference is available in the official WordPress
documentation: <https://developer.wordpress.org/rest-api/reference/>.

---

## Tested WordPress Versions

The supported APIs are covered by integration tests in the `wp-rest-client-test-integration`
module and are continuously validated against the following WordPress versions:

| WordPress Version | Tested |
|-------------------|:------:|
| 6.4.x             |   ✅    |
| 6.5.x             |   ✅    |
| 6.6.x             |   ✅    |
| 6.7.x             |   ✅    |
| 6.8.x             |   ✅    |
| 7.0.x             |   ✅    |

> Integration tests are executed against all supported WordPress versions to help
> ensure API compatibility and detect regressions across releases.

---

## APIs

| Resource       | Endpoint                    | Read | Create | Update | Delete | Notes                              |
|----------------|:----------------------------|:----:|:------:|:------:|:------:|------------------------------------|
| Posts          | `/posts`                    |  ✅   |   ✅    |   ✅    |   ✅    | Blog posts and post content.       |
| Post Revisions | `/posts/<parent>/revisions` |  ✅   |  N/A   |  N/A   |  N/A   | Blog posts revisions.              |
| Pages          | `/pages`                    |  ✅   |   ✅    |   ✅    |   ✅    | Static site pages.                 |
| Page Revisions | `/pages/<parent>/revisions` |  ✅   |  N/A   |  N/A   |  N/A   | Static site pages revisions.       |
| Media          | `/media`                    |  ✅   |   ✅    |   ✅    |   ✅    | Images, files, and attachments.    |
| Categories     | `/categories`               |  ✅   |   ✅    |   ✅    |   ✅    | Post categories.                   |
| Tags           | `/tags`                     |  ✅   |   ✅    |   ✅    |   ✅    | Post tags.                         |
| Comments       | `/comments`                 |  ✅   |   ✅    |   ⬜    |   ✅    | Comments and moderation workflows. |
| Users          | `/users`                    |  🚫  |   🚫   |   🚫   |   🚫   | Usually requires authentication.   |
| Search         | `/search`                   |  ⬜   |  N/A   |  N/A   |  N/A   | Search across public content.      |
| Taxonomies     | `/taxonomies`               |  ✅   |  N/A   |  N/A   |  N/A   | Taxonomy metadata.                 |
| Post Types     | `/types`                    |  ✅   |  N/A   |  N/A   |  N/A   | Registered post type metadata.     |
| Statuses       | `/statuses`                 |  ✅   |  N/A   |  N/A   |  N/A   | Registered post statuses.          |
| Settings       | `/settings`                 |  🚫  |  N/A   |   🚫   |  N/A   | Requires elevated permissions.     |

Legend:

- ✅ supported
- ⬜ not implemented yet
- 🚫 not planned
- N/A not applicable

### Posts

| Endpoint                   | Description     | Status |
|----------------------------|-----------------|:------:|
| `GET /wp/v2/posts`         | List Posts      |   ✅    |
| `POST /wp/v2/posts`        | Create a Post   |   ✅    |
| `GET /wp/v2/posts/<id>`    | Retrieve a Post |   ✅    |
| `POST /wp/v2/posts/<id>`   | Update a Post   |   ✅    |
| `DELETE /wp/v2/posts/<id>` | Delete a Post   |   ✅    |

### Post Revisions

| Endpoint                                      |   | Description              | Status | Notes                                                                                                                 |
|-----------------------------------------------|:--|--------------------------|:------:|-----------------------------------------------------------------------------------------------------------------------|
| `GET /wp/v2/posts/<parent>/revisions`         |   | List Posts Revisions     |   ✅    |                                                                                                                       |
| `GET /wp/v2/posts/<parent>/revisions/<id>`    |   | Retrieve a Post Revision |   ✅    |                                                                                                                       |
| `DELETE /wp/v2/posts/<parent>/revisions/<id>` |   | Delete a Post Revision   |  N/A   | Although the deletion is mentioned in the reference page, WordPress actually does not allow the deletion via REST API |

### Pages

| Endpoint                   | Description     | Status |
|----------------------------|-----------------|:------:|
| `GET /wp/v2/pages`         | List Pages      |   ✅    |
| `POST /wp/v2/pages`        | Create a Page   |   ✅    |
| `GET /wp/v2/pages/<id>`    | Retrieve a Page |   ✅    |
| `POST /wp/v2/pages/<id>`   | Update a Page   |   ✅    |
| `DELETE /wp/v2/pages/<id>` | Delete a Page   |   ✅    |

### Page Revisions

| Endpoint                                      |   | Description              | Status | Notes                                                                                                                 |
|-----------------------------------------------|:--|--------------------------|:------:|-----------------------------------------------------------------------------------------------------------------------|
| `GET /wp/v2/pages/<parent>/revisions`         |   | List Page Revisions      |   ✅    |                                                                                                                       |
| `GET /wp/v2/pages/<parent>/revisions/<id>`    |   | Retrieve a Page Revision |   ✅    |                                                                                                                       |
| `DELETE /wp/v2/pages/<parent>/revisions/<id>` |   | Delete a Page Revision   |  N/A   | Although the deletion is mentioned in the reference page, WordPress actually does not allow the deletion via REST API |

### Media

| Endpoint                   | Description           | Status |
|----------------------------|-----------------------|:------:|
| `GET /wp/v2/media`         | List Media            |   ✅    |
| `POST /wp/v2/media`        | Create a Media item   |   ✅    |
| `GET /wp/v2/media/<id>`    | Retrieve a Media item |   ✅    |
| `POST /wp/v2/media/<id>`   | Update a Media item   |   ✅    |
| `DELETE /wp/v2/media/<id>` | Delete a Media  item  |   ✅    |

### Categories

| Endpoint                        | Description         | Status |
|---------------------------------|---------------------|:------:|
| `GET /wp/v2/categories`         | List Categories     |   ✅    |
| `POST /wp/v2/categories`        | Create a Category   |   ✅    |
| `GET /wp/v2/categories/<id>`    | Retrieve a Category |   ✅    |
| `POST /wp/v2/categories/<id>`   | Update a Category   |   ✅    |
| `DELETE /wp/v2/categories/<id>` | Delete a Category   |   ✅    |

### Tags

| Endpoint                  | Description    | Status |
|---------------------------|----------------|:------:|
| `GET /wp/v2/tags`         | List Tags      |   ✅    |
| `POST /wp/v2/tags`        | Create a Tag   |   ✅    |
| `GET /wp/v2/tags/<id>`    | Retrieve a Tag |   ✅    |
| `POST /wp/v2/tags/<id>`   | Update a Tag   |   ✅    |
| `DELETE /wp/v2/tags/<id>` | Delete a Tag   |   ✅    |

### Comments

| Endpoint                      | Description        | Status |
|-------------------------------|--------------------|:------:|
| `GET /wp/v2/comments`         | List Comments      |   ✅    |
| `POST /wp/v2/comments`        | Create a Comment   |   ✅    |
| `GET /wp/v2/comments/<id>`    | Retrieve a Comment |   ✅    |
| `POST /wp/v2/comments/<id>`   | Update a Comment   |   ✅    |
| `DELETE /wp/v2/comments/<id>` | Delete a Comment   |   ✅    |

### Post Types

| Endpoint                  | Description     | Status |
|---------------------------|-----------------|:------:|
| `GET /wp/v2/types`        | List Post Types |   ✅    |
| `GET /wp/v2/types/<name>` | Get Post Type   |   ✅    |

### Taxonomies

| Endpoint                       | Description     | Status |
|--------------------------------|-----------------|:------:|
| `GET /wp/v2/taxonomies`        | List Taxonomies |   ✅    |
| `GET /wp/v2/taxonomies/<name>` | Get Taxonomy    |   ✅    |

### Statuses

| Endpoint                     | Description   | Status |
|------------------------------|---------------|:------:|
| `GET /wp/v2/statuses`        | List Statuses |   ✅    |
| `GET /wp/v2/statuses/<name>` | Get Status    |   ✅    |
