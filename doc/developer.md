# wp-rest-client developer's README

---

<!-- toc -->

- [Setup & Run](#setup--run)
  * [1. Install SDKMAN](#1-install-sdkman)
  * [2. Enable automatic SDK switching](#2-enable-automatic-sdk-switching)
  * [3. Use project-specific SDK settings](#3-use-project-specific-sdk-settings)
  * [4. Build and run](#4-build-and-run)

<!-- tocstop -->

--- 

## Setup & Run

This project uses [SDKMAN](https://sdkman.io/) to manage the JDK and MAVEN version.

### 1. Install SDKMAN

Run the following command:

```bash
curl -s "https://get.sdkman.io" | bash
```

Then initialize it:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Verify installation:

```bash
sdk version
```

---

### 2. Enable automatic SDK switching

To automatically switch to the correct SDK versions when entering the project directory, enable SDKMAN’s auto-env
feature.

Run:

```bash
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

```bash
sdk env install
```

After that, simply navigating into the project folder will activate the correct environment:

```bash
cd your-project-directory
```

---

### 4. Build and run

Using the Maven wrapper:

```bash
./mvnw clean install -Pdeveloper
```

or 
```shell
./mvnw clean install -Pdeveloper,integration-tests
```

Or with a global Maven installation:

```bash
mvn clean install -Pdeveloper
```


