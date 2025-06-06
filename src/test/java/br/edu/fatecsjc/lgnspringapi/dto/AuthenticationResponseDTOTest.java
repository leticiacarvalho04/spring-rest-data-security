package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationResponseDTOTest {

    private AuthenticationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new AuthenticationResponseDTO();
        responseDTO.setAccessToken("access-token-123");
        responseDTO.setRefreshToken("refresh-token-456");
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals("access-token-123", responseDTO.getAccessToken());
        assertEquals("refresh-token-456", responseDTO.getRefreshToken());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        AuthenticationResponseDTO dto = new AuthenticationResponseDTO();

        assertNull(dto.getAccessToken());
        assertNull(dto.getRefreshToken());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        AuthenticationResponseDTO dto = new AuthenticationResponseDTO("access", "refresh");

        assertEquals("access", dto.getAccessToken());
        assertEquals("refresh", dto.getRefreshToken());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        AuthenticationResponseDTO dto = AuthenticationResponseDTO.builder()
                .accessToken("built-access")
                .refreshToken("built-refresh")
                .build();

        assertEquals("built-access", dto.getAccessToken());
        assertEquals("built-refresh", dto.getRefreshToken());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        AuthenticationResponseDTO dto = AuthenticationResponseDTO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("access-token"));
        assertTrue(toString.contains("refresh-token"));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        AuthenticationResponseDTO dto1 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        AuthenticationResponseDTO dto2 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        AuthenticationResponseDTO differentDTO = AuthenticationResponseDTO.builder()
                .accessToken("different-access")
                .refreshToken("different-refresh")
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
        AuthenticationResponseDTO dto = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a DTO");
        assertEquals(dto, dto); // reflexive
    }

    @Test
    void shouldHandleNullFieldsInEqualsAndHashCodeAndToString() {
        AuthenticationResponseDTO dto1 = new AuthenticationResponseDTO();
        AuthenticationResponseDTO dto2 = new AuthenticationResponseDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("AuthenticationResponseDTO"));
    }

    @Test
    void shouldNotBeEqualWhenOnlyAccessTokenDiffers() {
        AuthenticationResponseDTO dto1 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        AuthenticationResponseDTO dto2 = AuthenticationResponseDTO.builder()
                .accessToken("other-access")
                .refreshToken("refresh")
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyRefreshTokenDiffers() {
        AuthenticationResponseDTO dto1 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        AuthenticationResponseDTO dto2 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("other-refresh")
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOneFieldIsNull() {
        AuthenticationResponseDTO dto1 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken(null)
                .build();

        AuthenticationResponseDTO dto2 = AuthenticationResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        assertNotEquals(dto1, dto2);

        AuthenticationResponseDTO dto3 = AuthenticationResponseDTO.builder()
                .accessToken(null)
                .refreshToken("refresh")
                .build();

        assertNotEquals(dto2, dto3);
    }
}