package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
}