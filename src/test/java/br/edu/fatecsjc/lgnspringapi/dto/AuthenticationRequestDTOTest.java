package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationRequestDTOTest {

    private AuthenticationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new AuthenticationRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password123");
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals("test@example.com", requestDTO.getEmail());
        assertEquals("password123", requestDTO.getPassword());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        AuthenticationRequestDTO dto = new AuthenticationRequestDTO();

        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        AuthenticationRequestDTO dto = new AuthenticationRequestDTO("user@example.com", "secret");

        assertEquals("user@example.com", dto.getEmail());
        assertEquals("secret", dto.getPassword());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                .email("built@example.com")
                .password("builtPassword")
                .build();

        assertEquals("built@example.com", dto.getEmail());
        assertEquals("builtPassword", dto.getPassword());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("password123"));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthenticationRequestDTO differentDTO = AuthenticationRequestDTO.builder()
                .email("different@example.com")
                .password("differentPassword")
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
        AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a DTO");
        assertEquals(dto, dto); // reflexive
    }

    @Test
    void shouldHandleNullFieldsInEqualsAndHashCodeAndToString() {
        AuthenticationRequestDTO dto1 = new AuthenticationRequestDTO();
        AuthenticationRequestDTO dto2 = new AuthenticationRequestDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("AuthenticationRequestDTO"));
    }

    @Test
    void shouldNotBeEqualWhenOnlyEmailDiffers() {
        AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                .email("other@example.com")
                .password("password123")
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyPasswordDiffers() {
        AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("otherPassword")
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOneFieldIsNull() {
        AuthenticationRequestDTO dto1 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password(null)
                .build();

        AuthenticationRequestDTO dto2 = AuthenticationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        assertNotEquals(dto1, dto2);

        AuthenticationRequestDTO dto3 = AuthenticationRequestDTO.builder()
                .email(null)
                .password("password123")
                .build();

        assertNotEquals(dto2, dto3);
    }

    @Test
    void shouldAllArgsConstructorWithNulls() {
        AuthenticationRequestDTO dto = AuthenticationRequestDTO.builder()
                .email(null)
                .password(null)
                .build();
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }
}