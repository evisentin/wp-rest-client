package com.ev.wordpress.test.local;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static com.ev.wordpress.test.local.TestFileUtils.deleteIfExists;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.testcontainers.containers.BindMode.READ_WRITE;
import static org.testcontainers.containers.wait.strategy.Wait.forHttp;

@Slf4j
abstract class AbstractWordPressIntegrationTest {

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
    public static final String WP_JWT_SECRET = "test-secret-123";

    public static final String WP_STANDARD_USER_NAME = "myuser@email.com";
    public static final String WP_STANDARD_USER_PASSWORD = "user123!";

    private static final Path WP_FILES_DIR = createTempFilesDir();
    private static final Network NETWORK = Network.newNetwork();

    @Container
    private static final MySQLContainer database = createDatabaseContainer();

    @Container
    private static final GenericContainer<?> wordpress = createWordPressContainer();

    @Container
    protected static final GenericContainer<?> wpcli = createWordPressCli();

    protected static String adminApplicationPassword = "";
    protected static String standardUserApplicationPassword = "";

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
        wpcli.execInContainer("wp", "--allow-root", "rewrite", "structure", "/%postname%/");
        wpcli.execInContainer("wp", "--allow-root", "rewrite", "flush");
    }

    @AfterAll
    static void cleanUp() {
        deleteIfExists(WP_FILES_DIR);
    }

    protected static String getHttpsBaseUrl() {
        return String.format("https://%s:%d", wordpress.getHost(), wordpress.getMappedPort(443));
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
    protected static void wpCleanDefaultData() {
        wpcli.execInContainer("sh", "-c",
                "wp --allow-root --path=/var/www/html post delete $(wp post list --format=ids) --force");

        wpcli.execInContainer("sh", "-c",
                "wp --allow-root --path=/var/www/html term delete category $(wp term list category --field=term_id --exclude=1)");

        wpcli.execInContainer("sh", "-c",
                "wp --allow-root --path=/var/www/html term delete post_tag $(wp term list post_tag --field=term_id)");
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
    protected static void wpConfigureDefaultUsers() {
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
    protected static Long wpCreateCategory(final @NonNull String name,
                                           final @NonNull String description,
                                           final @NonNull String slug) {
        val execResult = wpcli.execInContainer(
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
    protected static Long wpCreateTag(final @NonNull String name,
                                      final @NonNull String description,
                                      final @NonNull String slug) {
        val execResult = wpcli.execInContainer(
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
    protected static void wpInitWordPress(final @NonNull String baseUrl) {
        val installResult = wpcli.execInContainer(
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
    protected static void wpInstallAndActivateWpRestApiAuthenticationPlugin() {
        // install + activate plugin
        val pluginInstallResult = wpcli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "plugin", "install", "wp-rest-api-authentication",
                "--activate"
        );

        failOnError(pluginInstallResult, "wp-rest-api-authentication install failed");

        wpcli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "option", "update", "mo_api_authentication_selected_authentication_method", "basic_auth"
        );
        wpcli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "option", "update", "mo_api_authentication_enable_jwt", "1"
        );

        wpcli.execInContainer(
                "wp", "--allow-root", "--path=/var/www/html",
                "option", "update",
                "mo_api_authentication_jwt_client_secret",
                WP_JWT_SECRET
        );
    }

    @SneakyThrows
    private static void createAdminApplicationPassword() {
        val applicationPasswordCreationResult = wpcli.execInContainer(
                "wp", "user", "application-password", "create", "admin", "my-app", "--porcelain"
        );

        failOnError(applicationPasswordCreationResult, "application password creation failed");

        adminApplicationPassword = trimToEmpty(applicationPasswordCreationResult.getStdout());
    }

    @SuppressWarnings("resource")
    private static MySQLContainer createDatabaseContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName(VAL_DB_NAME)
                .withUsername(VAL_DB_USER_NAME)
                .withPassword(VAL_DB_PASSWORD)
                .withNetwork(NETWORK)
                .withNetwork(NETWORK)
                .withNetworkAliases("db_host"); // important for VAL_DB_HOST
    }

    @SneakyThrows
    private static void createStandardUser() {

        val standardUserCreationResult = wpcli.execInContainer(
                "wp", "user", "create", "username", WP_STANDARD_USER_NAME, "--role=subscriber", "--user_pass=" + WP_STANDARD_USER_PASSWORD
        );

        failOnError(standardUserCreationResult, "standard user creation failed");

        val applicationPasswordCreationResult = wpcli.execInContainer(
                "wp", "user", "application-password", "create", WP_STANDARD_USER_NAME, "my-user-app", "--porcelain"
        );

        failOnError(applicationPasswordCreationResult, "standard user's application password creation failed");

        standardUserApplicationPassword = trimToEmpty(applicationPasswordCreationResult.getStdout());
    }

    @SneakyThrows
    private static Path createTempFilesDir() {
        return Files.createTempDirectory("wordpress-files");
    }

    @SuppressWarnings("resource")
    private static GenericContainer<?> createWordPressCli() {
        return new GenericContainer<>(DockerImageName.parse("wordpress:cli"))
                .withNetwork(NETWORK)
                .dependsOn(wordpress)
                .withEnv(WORDPRESS_DB_HOST, VAL_DB_HOST)
                .withEnv(WORDPRESS_DB_NAME, VAL_DB_NAME)
                .withEnv(WORDPRESS_DB_USER, VAL_DB_USER_NAME)
                .withEnv(WORDPRESS_DB_PASSWORD, VAL_DB_PASSWORD)
                .withFileSystemBind(WP_FILES_DIR.toAbsolutePath().toString(), "/var/www/html", READ_WRITE)
                // official docs note cli image often needs UID alignment
                .withCreateContainerCmdModifier(cmd -> cmd.withUser("33:33"))
                .withCommand("tail", "-f", "/dev/null");
    }

    @SuppressWarnings("resource")
    private static GenericContainer<?> createWordPressContainer() {
        return new GenericContainer<>(wordpressHttpsImage("latest"))
                .withNetwork(NETWORK)
                .dependsOn(database)
                .withEnv(WORDPRESS_DB_HOST, VAL_DB_HOST)
                .withEnv(WORDPRESS_DB_NAME, VAL_DB_NAME)
                .withEnv(WORDPRESS_DB_USER, VAL_DB_USER_NAME)
                .withEnv(WORDPRESS_DB_PASSWORD, VAL_DB_PASSWORD)
                .withFileSystemBind(WP_FILES_DIR.toAbsolutePath().toString(), "/var/www/html", READ_WRITE)
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
            final String errorMessage = String.format("%s\nSTDOUT:\n%s\nSTDERR:\n%s", message, execResult.getStdout(), execResult.getStderr());
            throw new IllegalStateException(errorMessage);
        }
    }

    private static ImageFromDockerfile wordpressHttpsImage(final String version) {
        return new ImageFromDockerfile("wordpress-https-test", false)
                .withFileFromClasspath("Dockerfile", "docker/wordpress-https/Dockerfile")
                .withFileFromClasspath("wordpress-ssl.conf", "docker/wordpress-https/wordpress-ssl.conf")
                .withBuildArg("WORDPRESS_VERSION", version);
    }
}
