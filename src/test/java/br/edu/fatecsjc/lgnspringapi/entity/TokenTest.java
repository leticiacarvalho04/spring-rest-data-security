package br.edu.fatecsjc.lgnspringapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.fatecsjc.lgnspringapi.enums.TokenType;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

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
        assertThat(token).isNotNull();
        assertThat(token.getId()).isEqualTo(1L);
        assertThat(token.getToken()).isEqualTo("valid-jwt-token");
        assertThat(token.getTokenType()).isEqualTo(TokenType.BEARER);
        assertThat(token.isRevoked()).isFalse();
        assertThat(token.isExpired()).isFalse();
        assertThat(token.getUser().getEmail()).isEqualTo("test@example.com");
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

        assertThat(token.getId()).isEqualTo(2L);
        assertThat(token.getToken()).isEqualTo("new-jwt-token");
        assertThat(token.getTokenType()).isEqualTo(TokenType.BEARER);
        assertThat(token.isRevoked()).isTrue();
        assertThat(token.isExpired()).isTrue();
        assertThat(token.getUser().getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void shouldUseToStringWithoutErrors() {
        String toString = token.toString();
        assertThat(toString).contains("id=1", "token=valid-jwt-token", "tokenType=BEARER",
                "revoked=false", "expired=false", "user=User");
    }

    @Test
    void shouldCreateTokenUsingBuilder() {
        Token builtToken = Token.builder()
                .id(3L)
                .token("builder-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        assertThat(builtToken.getId()).isEqualTo(3L);
        assertThat(builtToken.getToken()).isEqualTo("builder-token");
        assertThat(builtToken.getTokenType()).isEqualTo(TokenType.BEARER);
        assertThat(builtToken.isRevoked()).isFalse();
        assertThat(builtToken.isExpired()).isFalse();
        assertThat(builtToken.getUser()).isEqualTo(user);
    }

    @Test
    void shouldCreateTokenUsingAllArgsConstructor() {
        Token tokenWithAllArgs = new Token(
                4L,
                "allargs-token",
                TokenType.BEARER,
                true,
                true,
                user
        );

        assertThat(tokenWithAllArgs.getId()).isEqualTo(4L);
        assertThat(tokenWithAllArgs.getToken()).isEqualTo("allargs-token");
        assertThat(tokenWithAllArgs.getTokenType()).isEqualTo(TokenType.BEARER);
        assertThat(tokenWithAllArgs.isRevoked()).isTrue();
        assertThat(tokenWithAllArgs.isExpired()).isTrue();
        assertThat(tokenWithAllArgs.getUser()).isEqualTo(user);
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        Token token1 = new Token(5L, "token-123", TokenType.BEARER, false, false, user);
        Token token2 = new Token(5L, "token-123", TokenType.BEARER, false, false, user);
        Token token3 = new Token(6L, "token-456", TokenType.BEARER, false, false, user);

        assertThat(token1).isEqualTo(token2);
        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
        assertThat(token1).isNotEqualTo(token3);
        assertThat(token1.hashCode()).isNotEqualTo(token3.hashCode());
    }
    
    @Test
    void shouldHaveDefaultValuesWhenUsingNoArgsConstructor() {
        Token emptyToken = new Token();

        assertThat(emptyToken.getId()).isNull();
        assertThat(emptyToken.getToken()).isNull();
        assertThat(emptyToken.getTokenType()).isEqualTo(TokenType.BEARER); 
        assertThat(emptyToken.isRevoked()).isFalse();
        assertThat(emptyToken.isExpired()).isFalse();
        assertThat(emptyToken.getUser()).isNull();
    }
    
    @Test
    void shouldHandleEqualsWithDifferentObjects() {
        Token token1 = new Token(7L, "token-789", TokenType.BEARER, false, false, user);

        assertThat(token1.equals(null)).isFalse();
        assertThat(token1.equals("NotAToken")).isFalse();
        assertThat(token1.equals(token1)).isTrue();
    }
    
    @Test
    void shouldHandleHashCodeWithNullFields() {
        Token token1 = new Token();
        Token token2 = new Token();

        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
    }

    @Test
    void shouldRespectEqualsReflexive() {
        Token token = new Token(8L, "token-888", TokenType.BEARER, false, false, user);
        assertThat(token.equals(token)).isTrue();
    }

    @Test
    void shouldRespectEqualsSymmetric() {
        Token token1 = new Token(9L, "token-999", TokenType.BEARER, false, false, user);
        Token token2 = new Token(9L, "token-999", TokenType.BEARER, false, false, user);

        assertThat(token1.equals(token2)).isTrue();
        assertThat(token2.equals(token1)).isTrue();
    }

    @Test
    void shouldRespectEqualsTransitive() {
        Token token1 = new Token(10L, "token-aaa", TokenType.BEARER, false, false, user);
        Token token2 = new Token(10L, "token-aaa", TokenType.BEARER, false, false, user);
        Token token3 = new Token(10L, "token-aaa", TokenType.BEARER, false, false, user);

        assertThat(token1.equals(token2)).isTrue();
        assertThat(token2.equals(token3)).isTrue();
        assertThat(token1.equals(token3)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenComparingDifferentTokens() {
        Token token1 = new Token(11L, "token-bbb", TokenType.BEARER, false, false, user);
        Token token2 = new Token(12L, "token-ccc", TokenType.BEARER, true, true, null);

        assertThat(token1.equals(token2)).isFalse();
    }

    @Test
    void shouldSetAllFieldsToNullAndCheck() {
        token.setId(null);
        token.setToken(null);
        token.setTokenType(null);
        token.setRevoked(false);
        token.setExpired(false);
        token.setUser(null);

        assertThat(token.getId()).isNull();
        assertThat(token.getToken()).isNull();
        assertThat(token.getTokenType()).isNull();
        assertThat(token.isRevoked()).isFalse();
        assertThat(token.isExpired()).isFalse();
        assertThat(token.getUser()).isNull();
    }

    @Test
    void shouldHandleLazyUserWhenNull() {
        Token lazyToken = new Token();
        assertThat(lazyToken.getUser()).isNull();
    }

}