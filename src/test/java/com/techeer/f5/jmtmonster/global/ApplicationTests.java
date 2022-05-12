package com.techeer.f5.jmtmonster.global;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
class ApplicationTests {

    @Test
    void contextLoads() {
    }
}
