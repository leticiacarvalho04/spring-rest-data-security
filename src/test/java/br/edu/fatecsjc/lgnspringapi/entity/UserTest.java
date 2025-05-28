package br.edu.fatecsjc.lgnspringapi.entity;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        // Arrange
        Token token1 = Token.builder().id(1L).token("abc123").build();
        Token token2 = Token.builder().id(2L).token("def456").build();

        List<Token> tokens = List.of(token1, token2);

        User user = new User(
                1L,
                "João",
                "Silva",
                "joao@example.com",
                "encoded_password",
                Role.USER,
                tokens
        );

        // Assert
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("João");
        assertThat(user.getLastName()).isEqualTo("Silva");
        assertThat(user.getEmail()).isEqualTo("joao@example.com");
        assertThat(user.getPassword()).isEqualTo("encoded_password");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getTokens()).containsExactlyInAnyOrder(token1, token2);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        User user = new User();

        Token token = Token.builder().id(3L).token("ghi789").build();

        // Act
        user.setId(2L);
        user.setFirstName("Maria");
        user.setLastName("Oliveira");
        user.setEmail("maria@example.com");
        user.setPassword("pass123");
        user.setRole(Role.ADMIN);
        user.setTokens(List.of(token));

        // Assert
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getFirstName()).isEqualTo("Maria");
        assertThat(user.getLastName()).isEqualTo("Oliveira");
        assertThat(user.getEmail()).isEqualTo("maria@example.com");
        assertThat(user.getPassword()).isEqualTo("pass123");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getTokens()).containsExactly(token);
    }

    @Test
    void testBuilder() {
        // Arrange
        Token token1 = Token.builder().id(4L).token("jkl012").build();
        Token token2 = Token.builder().id(5L).token("mno345").build();

        List<Token> tokens = List.of(token1, token2);

        // Act
        User user = User.builder()
                .id(3L)
                .firstName("Carlos")
                .lastName("Souza")
                .email("carlos@example.com")
                .password("pass456")
                .role(Role.ADMIN)
                .tokens(tokens)
                .build();

        // Assert
        assertThat(user.getId()).isEqualTo(3L);
        assertThat(user.getFirstName()).isEqualTo("Carlos");
        assertThat(user.getLastName()).isEqualTo("Souza");
        assertThat(user.getEmail()).isEqualTo("carlos@example.com");
        assertThat(user.getPassword()).isEqualTo("pass456");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getTokens()).containsExactlyInAnyOrder(token1, token2);
    }

    @Test
    void testUserDetailsMethods() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("pass");
        user.setRole(Role.USER);

        UserDetails userDetails = user;

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).extracting("authority")
                               .containsExactlyInAnyOrder(
                                   "READ",
                                   "ROLE_USER"
                               );
    }

    @Test
    void testBidirectionalTokenRelationship() {
        // Arrange
        User user = User.builder()
                .id(4L)
                .email("bidir@example.com")
                .build();

        Token token = Token.builder()
                .id(6L)
                .token("xyz789")
                .user(user)
                .build();

        user.setTokens(List.of(token));

        // Assert
        assertThat(user.getTokens()).contains(token);
        assertThat(token.getUser()).isEqualTo(user);
    }
}