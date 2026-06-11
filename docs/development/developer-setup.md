# wp-rest-client developer's README

## Setup & Run

This project uses [SDKMAN](https://sdkman.io/) to manage the JDK and MAVEN version.

### 1. Install SDKMAN

Run the following command:

```shell
curl -s "https://get.sdkman.io" | bash
```

Then initialize it:

```shell
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Verify installation:

```shell
sdk version
```

---

### 2. Enable automatic SDK switching

To automatically switch to the correct SDK versions when entering the project directory, enable SDKMAN’s auto-env
feature.

Run:

```shell
sdk config
```

Then set:

```txt
sdkman_auto_env=true
```

Save and exit.

> 💡 SDK versions will automatically switch when you `cd` into the project directory.

---

### 3. Use project-specific SDK settings

This project includes a `.sdkmanrc` file that defines the required SDK versions.

With auto-env enabled, SDKMAN will automatically:

- detect the `.sdkmanrc` file
- install missing SDKs (if needed)
- switch to the correct versions when you enter the project directory

If this is your first time in the project, run:

```shell
sdk env install
```

After that, simply navigating into the project folder will activate the correct environment:

```shell
cd your-project-directory
```

---

### 4. Build and run

Using the Maven wrapper:

```shell
./mvnw clean install -Plocal-tests
```

or

```shell
./mvnw clean install -Plocal-tests,integration-tests
```

Or with a global Maven installation:

```shell
mvn clean install -Plocal-tests
```


