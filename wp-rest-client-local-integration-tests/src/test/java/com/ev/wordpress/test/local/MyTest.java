package com.ev.wordpress.test.local;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyTest extends AbstractWordPressIntegrationTest{

     @BeforeAll
    void installWordpress() throws Exception {

        log.info("installWordpress: BEGIN");

        final Container.ExecResult execResult = wpcli.execInContainer("wp", "--allow-root", "--path=/var/www/html", "core", "is-installed");

        if (execResult.getExitCode() != 0) {
            log.info("installWordpress: initializing...");
            initWordpress(getHttpsBaseUrl());
            activatePermalinks();
            cleanDefaultData();
            log.info("installWordpress: initialized");
        } else {
            log.error("installWordpress: FAILURE\n STDOUT:{}\n STDERR:{}",
                    execResult.getStdout(), execResult.getStderr());
        }
        log.info("installWordpress: END");
    }

    @Test
    void mytest(){
        Assertions.assertTrue(true);
    }
}
