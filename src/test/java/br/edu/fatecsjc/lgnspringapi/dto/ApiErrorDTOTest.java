package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiErrorDTOTest {

    private static final String ERROR_MESSAGE = "An error occurred";
    private static final Instant NOW = Instant.now();

    private ApiErrorDTO errorDTO;

    @BeforeEach
    void setUp() {
        errorDTO = new ApiErrorDTO();
        errorDTO.setMessage(ERROR_MESSAGE);
        errorDTO.setTimestamp(NOW);
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals(ERROR_MESSAGE, errorDTO.getMessage());
        assertEquals(NOW, errorDTO.getTimestamp());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        ApiErrorDTO emptyDTO = new ApiErrorDTO();
        assertNull(emptyDTO.getMessage());
        assertNull(emptyDTO.getTimestamp());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        ApiErrorDTO dto = new ApiErrorDTO(ERROR_MESSAGE, NOW);

        assertEquals(ERROR_MESSAGE, dto.getMessage());
        assertEquals(NOW, dto.getTimestamp());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        ApiErrorDTO dto = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        assertEquals(ERROR_MESSAGE, dto.getMessage());
        assertEquals(NOW, dto.getTimestamp());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        ApiErrorDTO dto = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains(ERROR_MESSAGE));
        assertTrue(toString.contains(NOW.toString()));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        ApiErrorDTO dto1 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        ApiErrorDTO dto2 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        ApiErrorDTO differentDTO = ApiErrorDTO.builder()
                .message("Different message")
                .timestamp(NOW.minusSeconds(10))
                .build();

        // Test equals
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, differentDTO);

        // Test hash code
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), differentDTO.hashCode());
    }

    @Test
    void shouldEqualsHandleNullAndOtherTypes() {
        ApiErrorDTO dto = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        assertNotEquals(dto, null);
        assertNotEquals(dto, "not an ApiErrorDTO");
        assertEquals(dto, dto); // reflexive
    }

    @Test
    void shouldHandleNullFieldsInEqualsAndHashCodeAndToString() {
        ApiErrorDTO dto1 = new ApiErrorDTO();
        ApiErrorDTO dto2 = new ApiErrorDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ApiErrorDTO"));
    }

    @Test
    void shouldNotBeEqualWhenOnlyMessageDiffers() {
        ApiErrorDTO dto1 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        ApiErrorDTO dto2 = ApiErrorDTO.builder()
                .message("Other message")
                .timestamp(NOW)
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyTimestampDiffers() {
        ApiErrorDTO dto1 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        ApiErrorDTO dto2 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW.minusSeconds(100))
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOneFieldIsNull() {
        ApiErrorDTO dto1 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(null)
                .build();

        ApiErrorDTO dto2 = ApiErrorDTO.builder()
                .message(ERROR_MESSAGE)
                .timestamp(NOW)
                .build();

        assertNotEquals(dto1, dto2);

        ApiErrorDTO dto3 = ApiErrorDTO.builder()
                .message(null)
                .timestamp(NOW)
                .build();

        assertNotEquals(dto2, dto3);
    }
}