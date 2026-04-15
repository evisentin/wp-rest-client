package com.ev.wordpress.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestFileUtils {
    @SneakyThrows
    public static void deleteIfExists(final Path path) {
        if (path == null) return;

        if (!Files.exists(path)) {
            log.debug("Path does not exist, nothing to delete: {}", path);
            return;
        }

        log.info("Deleting path recursively: {}", path);

        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                  .forEach(p -> {
                      try {
                          log.trace("Deleting: {}", p);
                          Files.delete(p);
                      } catch (IOException e) {
                          log.error("Failed to delete: {}", p, e);
                          throw new RuntimeException("Failed to delete " + p, e);
                      }
                  });
        }

        log.info("Successfully deleted: {}", path);
    }
}
