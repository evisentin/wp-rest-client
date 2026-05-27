package io.github.evisentin.wordpress.test.integration.base;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static File testReourceAsTemporaryFile(String name) throws IOException {
        InputStream is = TestUtils.class
                .getClassLoader()
                .getResourceAsStream(name);

        Path tempFile = Files.createTempFile("test", ".tmp");

        assert is != null;
        Files.copy(is, tempFile, REPLACE_EXISTING);

        return tempFile.toFile();
    }
}
