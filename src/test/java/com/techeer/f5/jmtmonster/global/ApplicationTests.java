package com.techeer.f5.jmtmonster.global;

import com.techeer.f5.jmtmonster.global.config.JacksonConfig;
import com.techeer.f5.jmtmonster.global.config.JacksonModuleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
@Import({JacksonConfig.class, JacksonModuleConfig.class})
class ApplicationTests {

    @Test
    void contextLoads() {

    }
}
