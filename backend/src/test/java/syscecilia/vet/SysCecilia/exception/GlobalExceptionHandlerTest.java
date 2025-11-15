package syscecilia.vet.SysCecilia.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import syscecilia.vet.SysCecilia.config.TestConfig;
import syscecilia.vet.SysCecilia.controller.AnimalController;
import syscecilia.vet.SysCecilia.service.AnimalService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class})
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Test
    @DisplayName("DELETE /api/animals/{id} - Should return 405 Method Not Allowed")
    void shouldReturn405MethodNotAllowedForDeleteRequest() throws Exception {
        mockMvc.perform(delete("/api/animals/1"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status", equalTo(405)))
                .andExpect(jsonPath("$.title", equalTo("Method Not Allowed")))
                .andExpect(jsonPath("$.type", equalTo("https://syscecilia.vet/problems/method-not-allowed")))
                .andExpect(jsonPath("$.detail", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.path", equalTo("/api/animals/1")))
                .andExpect(jsonPath("$.supportedMethods", notNullValue()));
    }

    @Test
    @DisplayName("PATCH /api/animals/{id} - Should return 405 Method Not Allowed")
    void shouldReturn405MethodNotAllowedForPatchRequest() throws Exception {
        mockMvc.perform(patch("/api/animals/1"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status", equalTo(405)))
                .andExpect(jsonPath("$.title", equalTo("Method Not Allowed")))
                .andExpect(jsonPath("$.path", equalTo("/api/animals/1")));
    }
}

