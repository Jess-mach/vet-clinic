package syscecilia.vet.SysCecilia;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SysCeciliaApplication Tests")
class SysCeciliaApplicationTest {

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // If the context fails to load, the test will fail automatically
    }
}

