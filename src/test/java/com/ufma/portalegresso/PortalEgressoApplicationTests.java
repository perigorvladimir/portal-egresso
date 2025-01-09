package com.ufma.portalegresso;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class PortalEgressoApplicationTests {

    @Test
    void contextLoads() {
        assertTrue("true", true);
    }
}
