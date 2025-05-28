package br.edu.fatecsjc.lgnspringapi.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TokenTypeTest {

    @Test
    void shouldHaveValidEnumValue() {
        assertEquals("BEARER", TokenType.BEARER.name());
    }

    @Test
    void shouldReturnAllValuesUsingValuesMethod() {
        TokenType[] values = TokenType.values();

        assertNotNull(values);
        assertEquals(1, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(TokenType.BEARER));
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = TokenType.BEARER.toString();
        assertNotNull(toString);
        assertEquals("BEARER", toString);
    }

    @Test
    void shouldHaveCorrectOrdinal() {
        assertEquals(0, TokenType.BEARER.ordinal());
    }
}