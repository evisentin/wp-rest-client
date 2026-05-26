## Supported WP REST APIs

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
| `GET /wp/v2/media`         | List Media            | ✅      |
| `POST /wp/v2/media`        | Create a Media item   | ✅      |
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
