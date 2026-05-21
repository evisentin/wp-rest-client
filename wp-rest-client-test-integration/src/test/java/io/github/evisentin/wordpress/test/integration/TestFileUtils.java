package io.github.evisentin.wordpress.test.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestFileUtils {
    @SneakyThrows
    public static void deleteIfExists(final Path path) {
        if (path == null) return;

        if (!Files.exists(path)) {

            return;
        }

        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                  .forEach(p -> {
                      try {
                          Files.delete(p);
                      } catch (IOException e) {

                          throw new RuntimeException("Failed to delete " + p, e);
                      }
                  });
        }
    }
}
