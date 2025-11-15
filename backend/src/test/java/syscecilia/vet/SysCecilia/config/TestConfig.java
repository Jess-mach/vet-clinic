package syscecilia.vet.SysCecilia.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import syscecilia.vet.SysCecilia.exception.GlobalExceptionHandler;

@TestConfiguration
public class TestConfig {
    
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}

