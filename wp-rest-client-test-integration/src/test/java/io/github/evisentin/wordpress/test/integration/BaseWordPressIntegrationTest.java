package io.github.evisentin.wordpress.test.integration;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static io.github.evisentin.wordpress.test.integration.TestFileUtils.deleteIfExists;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.testcontainers.containers.BindMode.READ_WRITE;
import static org.testcontainers.containers.wait.strategy.Wait.forHttp;

/**
 * Base class for WordPress integration tests using Testcontainers.
 *
 * <p>This class provides a fully isolated and reproducible test environment by
 * orchestrating a WordPress instance, a MySQL database, and a WP-CLI container within a shared Docker network. It is
 * designed to simplify end-to-end testing of WordPress integrations by handling infrastructure setup, lifecycle
 * management, and common test utilities.</p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Provisioning of a MySQL database container preconfigured for WordPress</li>
 *   <li>Startup of a WordPress container with HTTPS support</li>
 *   <li>Dedicated WP-CLI container for executing administrative commands</li>
 *   <li>Utility methods for installation, configuration, and data management</li>
 *   <li>Predefined test users and authentication setup</li>
 * </ul>
 *
 * <h2>Test Lifecycle</h2>
 * <p>The containers are managed by Testcontainers and shared across test methods
 * within the same test class ({@link TestInstance.Lifecycle#PER_CLASS}). Temporary
 * file resources are cleaned up after all tests have completed.</p>
 *
 * <h2>Typical Usage</h2>
 * <pre>{@code
 * class MyIntegrationTest extends BaseWordPressIntegrationTest {
 *
 *     @Override
 *     public String getWordPressVersion() {
 *         return "6.5";
 *     }
 *
 *     @Test
 *     void shouldDoSomething() {
 *         wpInitWordPress(getHttpsBaseUrl());
 *         wpConfigureDefaultUsers();
 *         wpActivatePermalinks();
 *
 *         // perform test logic...
 *     }
 * }
 * }</pre>
 *
 * <h2>Notes</h2>
 * <ul>
 *   <li>All interactions with WordPress are performed via WP-CLI inside the container</li>
 *   <li>Helper methods fail fast if underlying commands return non-zero exit codes</li>
 *   <li>The environment is intended for testing purposes only and is not production-ready</li>
 * </ul>
 *
 * @see org.testcontainers.junit.jupiter.Testcontainers
 * @see org.testcontainers.containers.GenericContainer
 * @see org.testcontainers.mysql.MySQLContainer
 */
@Slf4j
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseWordPressIntegrationTest implements WithAssertions {

    public static final String WORDPRESS_DB_HOST = "WORDPRESS_DB_HOST";
    public static final String WORDPRESS_DB_NAME = "WORDPRESS_DB_NAME";
    public static final String WORDPRESS_DB_USER = "WORDPRESS_DB_USER";
    public static final String WORDPRESS_DB_PASSWORD = "WORDPRESS_DB_PASSWORD";

    public static final String VAL_DB_HOST = "db_host:3306";
    public static final String VAL_DB_NAME = "wordpress_db";
    public static final String VAL_DB_USER_NAME = "wordpress";
    public static final String VAL_DB_PASSWORD = "wordpress_pwd";

    public static final String WP_ADMIN_USER_NAME = "admin";
    public static final String WP_ADMIN_PASSWORD = "admin123!";
    public static final String WP_JWT_SECRET = "NOOfWLMHypGdbSdEUlKkQAMiuneifuoG";

    public static final String WP_STANDARD_USER_NAME = "myuser@email.com";
    public static final String WP_STANDARD_USER_PASSWORD = "user123!";

    private final Path wpFilesDir = createTempFilesDir();
    private final Network network = Network.newNetwork();

    private final MySQLContainer database = createDatabaseContainer();
    private final GenericContainer<?> wordpress = createWordPressContainer();
    private final GenericContainer<?> wpCli = createWordPressCli();
    protected String adminApplicationPassword = "";
    protected String standardUserApplicationPassword = "";

    public abstract String getWordPressVersion();

    @AfterAll
    void cleanUp() {
        if (wpCli != null) wpCli.close();

        if (wordpress != null) wordpress.close();

        if (database != null) database.close();

        if (network != null) network.close();

        deleteIfExists(wpFilesDir);
    }

    @BeforeAll
    void startEnvironment() {
        Startables.deepStart(database, wordpress, wpCli)
                  .join();
    }

    protected String getHttpsBaseUrl() {
        return String.format("https://%s:%d", wordpress.getHost(), wordpress.getMappedPort(443));
    }

    protected String toBlock(String value) {
        return String.format("<p>%s</p>\n", value);
    }

    /**
     * Activates "pretty permalinks" in the WordPress instance.
     *
     * <p>This method configures WordPress to use the {@code /%postname%/} permalink
     * structure and flushes the rewrite rules to ensure the changes take effect immediately.</p>
     *
     * <p>It relies on WP-CLI commands executed داخل the container environment.</p>
     *
     * <p>This is typically required in tests that depend on human-readable URLs
     * instead of the default query-based format.</p>
     */
    @SneakyThrows
    protected void wpActivatePermalinks() {
        wpCli.execInContainer("wp", "--allow-root", "rewrite", "structure", "/%postname%/");
        wpCli.execInContainer("wp", "--allow-root", "rewrite", "flush");
    }

    /**
     * Removes all default WordPress content from the test instance.
     *
     * <p>This method deletes all posts, pages, and custom post types,
     * as well as all taxonomy terms (categories and tags), effectively resetting the content state of the WordPress
     * installation.</p>
     *
     * <p>The default "Uncategorized" category (term ID = 1) is preserved
     * to avoid breaking WordPress constraints.</p>
     *
     * <p>This is typically used to ensure a clean and predictable starting
     * point for tests.</p>
     *
     * <h3>Operations performed</h3>
     * <ul>
     *   <li>Deletes all posts using WP-CLI</li>
     *   <li>Deletes all categories except the default one</li>
     *   <li>Deletes all tags</li>
     * </ul>
     *
     */
    @SneakyThrows
    protected void wpCleanDefaultData() {
        wpCli.execInContainer("sh", "-c", "wp --allow-root --path=/var/www/html post delete $(wp post list --post_type=post       --format=ids) --force");
        wpCli.execInContainer("sh", "-c", "wp --allow-root --path=/var/www/html post delete $(wp post list --post_type=page       --format=ids) --force");
        wpCli.execInContainer("sh", "-c", "wp --allow-root --path=/var/www/html post delete $(wp post list --post_type=attachment --format=ids) --force");
        wpCli.execInContainer("sh", "-c", "wp --allow-root --path=/var/www/html term delete category $(wp term list category --field=term_id --exclude=1)");
        wpCli.execInContainer("sh", "-c", "wp --allow-root --path=/var/www/html term delete post_tag $(wp term list post_tag --field=term_id)");
    }

    /**
     * Configures default users for the WordPress test instance.
     *
     * <p>This method performs additional setup after installation:
     * <ul>
     *   <li>Creates an application password for the admin user</li>
     *   <li>Creates a standard (non-admin) user</li>
     * </ul>
     * </p>
     *
     * <p>This should typically be called after {@link #wpInitWordPress(String)}
     * to prepare the instance for authenticated test scenarios.</p>
     */
    protected void wpConfigureDefaultUsers() {
        createAdminApplicationPassword();
        createStandardUser();
    }

    /**
     * Creates a new WordPress category.
     *
     * <p>This method uses WP-CLI to create a category with the specified
     * name, slug, and description. It returns the ID of the created term.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code name} – the display name of the category</li>
     *   <li>{@code description} – the category description</li>
     *   <li>{@code slug} – the URL-friendly identifier</li>
     * </ul>
     *
     * @param name
     *         the name of the category (must not be {@code null})
     * @param description
     *         the description of the category (must not be {@code null})
     * @param slug
     *         the slug of the category (must not be {@code null})
     *
     * @return the ID of the created category as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreateCategory(final @NonNull String name,
                                    final @NonNull String description,
                                    final @NonNull String slug) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "term", "create", "category", name,
                "--slug=" + slug,
                "--description=" + description,
                "--porcelain"
        );
        failOnError(execResult, String.format("category creation '%s' failed", name));
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new WordPress comment.
     *
     * <p>This method uses WP-CLI to create a comment associated with the specified post. The comment is created with
     * the provided content, author name, and author email address. It returns the ID of the created comment.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code postId} – the ID of the post to which the comment belongs</li>
     *   <li>{@code content} – the comment text</li>
     *   <li>{@code author} – the display name of the comment author</li>
     *   <li>{@code authorEmail} – the email address of the comment author</li>
     * </ul>
     *
     * @param postId
     *         the ID of the target post
     * @param content
     *         the content of the comment (must not be {@code null})
     * @param author
     *         the author name of the comment (must not be {@code null})
     * @param authorEmail
     *         the email address of the comment author (must not be {@code null})
     *
     * @return the ID of the created comment as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreateComment(final long postId,
                                   final @NonNull String content,
                                   final @NonNull String author,
                                   final @NonNull String authorEmail) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "comment", "create",
                String.format("--comment_post_ID=%d", postId),
                String.format("--comment_content=%s", content),
                String.format("--comment_author=%s", author),
                String.format("--comment_author_email=%s", authorEmail),
                "--porcelain"
        );
        failOnError(execResult, "post creation failed");
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new password-protected WordPress page.
     *
     * <p>This method uses WP-CLI to create a page with the specified title, content,  status, and access
     * password. It returns the ID of the created post.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code title} – the title of the page</li>
     *   <li>{@code content} – the body content of the page</li>
     *   <li>{@code status} – the  status of the page</li>
     *   <li>{@code password} – the password required to access the page</li>
     * </ul>
     *
     * @param title
     *         the title of the page (must not be {@code null})
     * @param content
     *         the content of the page (must not be {@code null})
     * @param status
     *         the status of the page (must not be {@code null})
     * @param password
     *         the password protecting the page (must not be {@code null})
     *
     * @return the ID of the created page as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreatePage(final @NonNull String title,
                                final @NonNull String content,
                                final @NonNull WpPageStatus status,
                                final @NonNull String password) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "create",
                "--post_type=page",
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                String.format("--post_status=%s", status.getValue()),
                String.format("--post_password=%s", password),
                "--porcelain"
        );
        failOnError(execResult, "page creation failed");
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new WordPress page.
     *
     * <p>This method uses WP-CLI to create a page with the specified title, content, and  status. It
     * returns the ID of the created page.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code title} – the title of the page</li>
     *   <li>{@code content} – the body content of the page</li>
     *   <li>{@code status} – the  status of the page</li>
     * </ul>
     *
     * @param title
     *         the title of the page (must not be {@code null})
     * @param content
     *         the content of the page (must not be {@code null})
     * @param status
     *         the status of the page (must not be {@code null})
     *
     * @return the ID of the created post as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreatePage(final @NonNull String title,
                                final @NonNull String content,
                                final @NonNull WpPageStatus status) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "create",
                "--post_type=page",
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                String.format("--post_status=%s", status.getValue()),
                "--porcelain"
        );
        failOnError(execResult, "post creation failed");
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new password-protected WordPress post.
     *
     * <p>This method uses WP-CLI to create a post with the specified title, content,  status, and access
     * password. It returns the ID of the created post.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code title} – the title of the post</li>
     *   <li>{@code content} – the body content of the post</li>
     *   <li>{@code status} – the  status of the post</li>
     *   <li>{@code password} – the password required to access the post</li>
     * </ul>
     *
     * @param title
     *         the title of the post (must not be {@code null})
     * @param content
     *         the content of the post (must not be {@code null})
     * @param status
     *         the status of the post (must not be {@code null})
     * @param password
     *         the password protecting the post (must not be {@code null})
     *
     * @return the ID of the created post as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreatePost(final @NonNull String title,
                                final @NonNull String content,
                                final @NonNull WpPostStatus status,
                                final @NonNull String password) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "create",
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                String.format("--post_status=%s", status.getValue()),
                String.format("--post_password=%s", password),
                "--porcelain"
        );
        failOnError(execResult, "post creation failed");
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new WordPress post.
     *
     * <p>This method uses WP-CLI to create a post with the specified title, content, and  status. It
     * returns the ID of the created post.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code title} – the title of the post</li>
     *   <li>{@code content} – the body content of the post</li>
     *   <li>{@code status} – the  status of the post</li>
     * </ul>
     *
     * @param title
     *         the title of the post (must not be {@code null})
     * @param content
     *         the content of the post (must not be {@code null})
     * @param status
     *         the status of the post (must not be {@code null})
     *
     * @return the ID of the created post as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreatePost(final @NonNull String title,
                                final @NonNull String content,
                                final @NonNull WpPostStatus status) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "create",
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                String.format("--post_status=%s", status.getValue()),
                "--porcelain"
        );
        failOnError(execResult, "post creation failed");
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Creates a new WordPress tag.
     *
     * <p>This method uses WP-CLI to create a tag with the specified
     * name, slug, and description. It returns the ID of the created term.</p>
     *
     * <p>If the creation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * <h3>Parameters</h3>
     * <ul>
     *   <li>{@code name} – the display name of the tag</li>
     *   <li>{@code description} – the tag description</li>
     *   <li>{@code slug} – the URL-friendly identifier</li>
     * </ul>
     *
     * @param name
     *         the name of the tag (must not be {@code null})
     * @param description
     *         the description of the tag (must not be {@code null})
     * @param slug
     *         the slug of the tag (must not be {@code null})
     *
     * @return the ID of the created tag as a {@link Long}
     */
    @SneakyThrows
    protected Long wpCreateTag(final @NonNull String name,
                               final @NonNull String description,
                               final @NonNull String slug) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "term", "create", "post_tag", name,
                "--slug=" + slug,
                "--description=" + description,
                "--porcelain"
        );
        failOnError(execResult, String.format("tag creation '%s' failed", name));
        return Long.valueOf(trimToEmpty(execResult.getStdout()));
    }

    /**
     * Retrieves the IDs of all revisions associated with the specified WordPress content item.
     * <p>
     * Works for both posts and pages. Revisions are returned in descending order of creation date (newest first). If
     * the content item has no revisions, an empty list is returned.
     *
     * @param id
     *         the ID of the WordPress post or page whose revisions should be retrieved
     *
     * @return a list containing the IDs of all revisions for the specified post or page, ordered from newest to oldest
     *
     * @throws RuntimeException
     *         if the WP-CLI command execution fails or returns an error
     */
    @SneakyThrows
    protected List<Long> wpGetRevisionIds(final long id) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "list",
                "--post_type=revision",
                String.format("--post_parent=%d", id),
                "--orderby=date",
                "--order=DESC",
                "--format=ids"
        );

        failOnError(execResult, "revision lookup failed");

        return Arrays.stream(trimToEmpty(execResult.getStdout()).split("\\s+"))
                     .filter(StringUtils::isNotBlank)
                     .map(Long::valueOf)
                     .toList();
    }

    /**
     * Initializes a fresh WordPress installation for testing.
     *
     * <p>This method performs a {@code wp core install} using the provided base URL
     * and predefined administrative credentials. It sets up a minimal WordPress instance ready for use in tests.</p>
     *
     * <p>This method does <strong>not</strong> perform additional configuration
     * such as user setup. Call {@link #wpConfigureDefaultUsers()} separately if needed.</p>
     *
     * <p>If the installation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     * @param baseUrl
     *         the base URL of the WordPress instance (must not be {@code null})
     */
    @SneakyThrows
    protected void wpInitWordPress(final @NonNull String baseUrl) {
        val installResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "core", "install",
                "--url=" + baseUrl,
                "--title=Test Site",
                "--admin_user=" + WP_ADMIN_USER_NAME,
                "--admin_password=" + WP_ADMIN_PASSWORD,
                "--admin_email=admin@example.test",
                "--skip-email"
        );

        failOnError(installResult, "wp core install failed");
    }

    /**
     * Installs and configures the WP REST API Authentication plugin.
     *
     * <p>This method installs and activates the {@code wp-rest-api-authentication}
     * plugin via WP-CLI, then configures it to enable authentication mechanisms required for testing.</p>
     *
     * <p>The following configuration is applied:
     * <ul>
     *   <li>Sets authentication method to {@code basic_auth}</li>
     *   <li>Enables JWT authentication</li>
     *   <li>Configures the JWT client secret</li>
     * </ul>
     * </p>
     *
     * <p>If the plugin installation fails, the test is immediately failed via
     * {@code failOnError(...)}.</p>
     *
     */
    @SneakyThrows
    protected void wpInstallAndActivateWpRestApiAuthenticationPlugin(boolean jwt) {
        // install + activate plugin
        val pluginInstallResult = wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "plugin", "install", "wp-rest-api-authentication", "--activate");

        failOnError(pluginInstallResult, "wp-rest-api-authentication install failed");

        final String authMethod = jwt ? "jwt_auth" : "basic_auth";

        wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "option", "update", "mo_api_authentication_selected_authentication_method", authMethod);

        if (jwt) {
            wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "option", "update", "mo_api_authentication_enable_jwt", "1");
            wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "option", "update", "mo_api_authentication_jwt_client_secret", WP_JWT_SECRET);
            wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "option", "update", "mo_api_authentication_jwt_signing_algorithm", "HS256");
        }
    }

    /**
     * Checks whether WordPress is installed in the current test instance.
     *
     * <p>This method executes the {@code wp core is-installed} command via WP-CLI
     * and determines the installation status based on the command exit code.</p>
     *
     * <p>The command does not produce meaningful output; instead:
     * <ul>
     *   <li>Exit code {@code 0} indicates that WordPress is installed</li>
     *   <li>A non-zero exit code indicates that WordPress is not installed</li>
     * </ul>
     * </p>
     *
     * @return {@code true} if WordPress is installed, {@code false} otherwise
     */
    @SneakyThrows
    protected boolean wpIsWordPressInstalled() {
        val execResult = wpCli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "core", "is-installed");
        return execResult.getExitCode() == 0;
    }

    @SneakyThrows
    protected void wpUpdatePage(final long id,
                                final @NonNull String title,
                                final @NonNull String content) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "update", Long.toString(id),
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                "--porcelain"
        );
        failOnError(execResult, "page update failed");
    }

    @SneakyThrows
    protected void wpUpdatePost(final long id,
                                final @NonNull String title,
                                final @NonNull String content) {
        val execResult = wpCli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "post", "update", Long.toString(id),
                String.format("--post_title=%s", title),
                String.format("--post_content=%s", content),
                "--porcelain"
        );
        failOnError(execResult, "post update failed");
    }

    @SneakyThrows
    private void createAdminApplicationPassword() {
        val applicationPasswordCreationResult = wpCli.execInContainer("wp", "user", "application-password", "create", "admin", "my-app", "--porcelain");

        failOnError(applicationPasswordCreationResult, "application password creation failed");

        adminApplicationPassword = trimToEmpty(applicationPasswordCreationResult.getStdout());
    }

    @SuppressWarnings("resource")
    private MySQLContainer createDatabaseContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName(VAL_DB_NAME)
                .withUsername(VAL_DB_USER_NAME)
                .withPassword(VAL_DB_PASSWORD)
                .withNetwork(network)
                .withNetworkAliases("db_host"); // important for VAL_DB_HOST
    }

    @SneakyThrows
    private void createStandardUser() {

        val standardUserCreationResult = wpCli.execInContainer(
                "wp", "user", "create", "username", WP_STANDARD_USER_NAME, "--role=subscriber", "--user_pass=" + WP_STANDARD_USER_PASSWORD
        );

        failOnError(standardUserCreationResult, "standard user creation failed");

        val applicationPasswordCreationResult = wpCli.execInContainer(
                "wp", "user", "application-password", "create", WP_STANDARD_USER_NAME, "my-user-app", "--porcelain"
        );

        failOnError(applicationPasswordCreationResult, "standard user's application password creation failed");

        standardUserApplicationPassword = trimToEmpty(applicationPasswordCreationResult.getStdout());
    }

    @SneakyThrows
    private Path createTempFilesDir() {
        return Files.createTempDirectory("wordpress-files");
    }

    @SuppressWarnings("resource")
    @SneakyThrows
    private GenericContainer<?> createWordPressCli() {
        return new GenericContainer<>(DockerImageName.parse("wordpress:cli"))
                .withNetwork(network)
                .dependsOn(wordpress)
                .withEnv(WORDPRESS_DB_HOST, VAL_DB_HOST)
                .withEnv(WORDPRESS_DB_NAME, VAL_DB_NAME)
                .withEnv(WORDPRESS_DB_USER, VAL_DB_USER_NAME)
                .withEnv(WORDPRESS_DB_PASSWORD, VAL_DB_PASSWORD)
                .withFileSystemBind(wpFilesDir.toAbsolutePath().toString(), "/var/www/html", BindMode.READ_WRITE)
                // official docs note cli image often needs UID alignment
                .withCreateContainerCmdModifier(cmd -> cmd.withUser("33:33"))
                .withCommand("tail", "-f", "/dev/null");
    }

    @SuppressWarnings("resource")
    private GenericContainer<?> createWordPressContainer() {

        return new GenericContainer<>(wordpressHttpsImage(getWordPressVersion()))
                .withNetwork(network)
                .dependsOn(database)
                .withEnv(WORDPRESS_DB_HOST, VAL_DB_HOST)
                .withEnv(WORDPRESS_DB_NAME, VAL_DB_NAME)
                .withEnv(WORDPRESS_DB_USER, VAL_DB_USER_NAME)
                .withEnv(WORDPRESS_DB_PASSWORD, VAL_DB_PASSWORD)
                .withFileSystemBind(wpFilesDir.toAbsolutePath().toString(), "/var/www/html", READ_WRITE)
                .withExposedPorts(443)
                .waitingFor(
                        forHttp("/")
                                .forPort(443)
                                .usingTls()
                                .allowInsecure()
                                .forStatusCodeMatching(code -> code == 200 || code == 301 || code == 302)
                )
                .withStartupTimeout(Duration.ofMinutes(3));
    }

    private static void failOnError(final @NonNull org.testcontainers.containers.Container.ExecResult execResult, final @NonNull String message) {
        if (execResult.getExitCode() != 0) {
            final String errorMessage = String.format("%s%nSTDOUT:%n%s%nSTDERR:%n%s", message, execResult.getStdout(), execResult.getStderr());
            throw new IllegalStateException(errorMessage);
        }
    }

    private static ImageFromDockerfile wordpressHttpsImage(final String version) {
        log.info(">>>> Building WordPress test image from base image wordpress:{}", version);

        return new ImageFromDockerfile("wordpress-https-test:" + version, false)
                .withFileFromClasspath("Dockerfile", "docker/wordpress-https/Dockerfile")
                .withFileFromClasspath("wordpress-ssl.conf", "docker/wordpress-https/wordpress-ssl.conf")
                .withBuildArg("WORDPRESS_VERSION", version);
    }
}
