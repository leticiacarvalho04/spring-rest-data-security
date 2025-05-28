package br.edu.fatecsjc.lgnspringapi.entity;

import br.edu.fatecsjc.lgnspringapi.enums.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TokenTest {

    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        // Configura usuário
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        // Configura token
        token = new Token();
        token.setId(1L);
        token.setToken("valid-jwt-token");
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        token.setUser(user);
    }

    @Test
    void shouldHaveValidInitialState() {
        assertNotNull(token);
        assertEquals(1L, token.getId());
        assertEquals("valid-jwt-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertNotNull(token.getUser());
        assertEquals("test@example.com", token.getUser().getEmail());
    }

    @Test
    void shouldSetFieldsCorrectlyUsingSetter() {
        User newUser = new User();
        newUser.setId(2L);
        newUser.setEmail("newuser@example.com");

        token.setId(2L);
        token.setToken("new-jwt-token");
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(true);
        token.setExpired(true);
        token.setUser(newUser);

        assertEquals(2L, token.getId());
        assertEquals("new-jwt-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
        assertEquals("newuser@example.com", token.getUser().getEmail());
    }

    @Test
    void shouldUseToStringWithoutErrors() {
        String toString = token.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("token=valid-jwt-token"));
        assertTrue(toString.contains("tokenType=BEARER"));
        assertTrue(toString.contains("revoked=false"));
        assertTrue(toString.contains("expired=false"));
        assertTrue(toString.contains("user=User"));
    }
}