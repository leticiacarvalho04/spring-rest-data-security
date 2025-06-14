package br.edu.fatecsjc.lgnspringapi.entity;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Token token1;
    private Token token2;

    @BeforeEach
    void setUp() {
        token1 = Token.builder().id(1L).token("abc123").build();
        token2 = Token.builder().id(2L).token("def456").build();

        user = User.builder()
                .id(1L)
                .firstName("João")
                .lastName("Silva")
                .email("joao@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .tokens(List.of(token1, token2))
                .build();
    }

    @Test
    void shouldTestAllFieldsWithBuilder() {
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("João");
        assertThat(user.getLastName()).isEqualTo("Silva");
        assertThat(user.getEmail()).isEqualTo("joao@example.com");
        assertThat(user.getPassword()).isEqualTo("encoded_password");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getTokens()).containsExactlyInAnyOrder(token1, token2);
    }

    @Test
    void shouldTestSettersAndNoArgsConstructor() {
        User emptyUser = new User();

        emptyUser.setId(2L);
        emptyUser.setFirstName("Maria");
        emptyUser.setLastName("Oliveira");
        emptyUser.setEmail("maria@example.com");
        emptyUser.setPassword("password123");
        emptyUser.setRole(Role.ADMIN);
        emptyUser.setTokens(List.of(token1));

        assertThat(emptyUser.getId()).isEqualTo(2L);
        assertThat(emptyUser.getFirstName()).isEqualTo("Maria");
        assertThat(emptyUser.getLastName()).isEqualTo("Oliveira");
        assertThat(emptyUser.getEmail()).isEqualTo("maria@example.com");
        assertThat(emptyUser.getPassword()).isEqualTo("password123");
        assertThat(emptyUser.getRole()).isEqualTo(Role.ADMIN);
        assertThat(emptyUser.getTokens()).containsExactly(token1);
    }

    @Test
    void shouldTestUserDetailsMethods() {
        UserDetails userDetails = user;

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertThat(authorities).extracting("authority")
                .containsExactlyInAnyOrder("ROLE_USER");

        assertThat(userDetails.getUsername()).isEqualTo("joao@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("encoded_password");
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        User userCopy = User.builder()
                .id(1L)
                .firstName("João")
                .lastName("Silva")
                .email("joao@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .tokens(List.of(token1, token2))
                .build();

        User differentUser = User.builder()
                .id(3L)
                .email("different@example.com")
                .build();

        assertThat(user).isEqualTo(userCopy);
        assertThat(user.hashCode()).isEqualTo(userCopy.hashCode());

        assertThat(user).isNotEqualTo(differentUser);
        assertThat(user.hashCode()).isNotEqualTo(differentUser.hashCode());
    }

    @Test
    void shouldTestEqualsAgainstNullAndOtherObjects() {
        assertThat(user.equals(null)).isFalse();
        assertThat(user.equals("NotAUser")).isFalse();
        assertThat(user.equals(user)).isTrue();
    }

    @Test
    void shouldTestToString() {
        String userString = user.toString();
        assertThat(userString).contains(
                "João",
                "Silva",
                "joao@example.com",
                "encoded_password",
                "USER");
    }

    @Test
    void shouldTestTokenBidirectionalRelationship() {
        token1.setUser(user);
        user.setTokens(List.of(token1));

        assertThat(user.getTokens()).containsExactly(token1);
        assertThat(token1.getUser()).isEqualTo(user);
    }

    @Test
    void shouldHandleNullFieldsGracefully() {
        User emptyUser = new User();

        assertThat(emptyUser.getId()).isNull();
        assertThat(emptyUser.getFirstName()).isNull();
        assertThat(emptyUser.getLastName()).isNull();
        assertThat(emptyUser.getEmail()).isNull();
        assertThat(emptyUser.getPassword()).isNull();
        assertThat(emptyUser.getRole()).isNull();
        assertThat(emptyUser.getTokens()).isNull();
    }

    @Test
    void shouldHandleNullRoleInAuthorities() {
        User userWithNullRole = User.builder()
                .id(99L)
                .email("nullrole@example.com")
                .password("pass")
                .role(null)
                .build();

        Collection<? extends GrantedAuthority> authorities = userWithNullRole.getAuthorities();

        assertThat(authorities).isEmpty();
    }

    @Test
    void shouldHandleNullAndEmptyTokens() {
        User userWithNullTokens = User.builder()
                .id(100L)
                .email("nulltokens@example.com")
                .password("pass")
                .role(Role.USER)
                .tokens(null)
                .build();

        assertThat(userWithNullTokens.getTokens()).isNull();

        User userWithEmptyTokens = User.builder()
                .id(101L)
                .email("emptytokens@example.com")
                .password("pass")
                .role(Role.USER)
                .tokens(List.of())
                .build();

        assertThat(userWithEmptyTokens.getTokens()).isEmpty();
    }

    @Test
    void shouldTestEqualsWithAllNullFields() {
        User user1 = new User();
        User user2 = new User();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void shouldHandleNullNamesAndEmail() {
        User userNull = User.builder()
                .id(200L)
                .firstName(null)
                .lastName(null)
                .email(null)
                .password("pass")
                .role(Role.USER)
                .build();

        assertThat(userNull.getFirstName()).isNull();
        assertThat(userNull.getLastName()).isNull();
        assertThat(userNull.getEmail()).isNull();
    }

    @Test
    void shouldHandleNullEmailInUsername() {
        User userWithNullEmail = User.builder()
                .id(300L)
                .email(null)
                .password("pass")
                .role(Role.USER)
                .build();

        assertThat(userWithNullEmail.getUsername()).isNull();
    }

    @Test
    void shouldHandleNullPassword() {
        User userWithNullPassword = User.builder()
                .id(400L)
                .email("nullpassword@example.com")
                .password(null)
                .role(Role.USER)
                .build();

        assertThat(userWithNullPassword.getPassword()).isNull();
    }

    @Test
    void shouldNotBeEqualWhenOnlyIdDiffers() {
        User u1 = User.builder().id(1L).email("a@a.com").build();
        User u2 = User.builder().id(2L).email("a@a.com").build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyFirstNameDiffers() {
        User u1 = User.builder().id(1L).firstName("A").build();
        User u2 = User.builder().id(1L).firstName("B").build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyLastNameDiffers() {
        User u1 = User.builder().id(1L).lastName("A").build();
        User u2 = User.builder().id(1L).lastName("B").build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyEmailDiffers() {
        User u1 = User.builder().id(1L).email("a@a.com").build();
        User u2 = User.builder().id(1L).email("b@b.com").build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyPasswordDiffers() {
        User u1 = User.builder().id(1L).password("a").build();
        User u2 = User.builder().id(1L).password("b").build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyRoleDiffers() {
        User u1 = User.builder().id(1L).role(Role.USER).build();
        User u2 = User.builder().id(1L).role(Role.ADMIN).build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyTokensDiffers() {
        Token t = Token.builder().id(99L).token("t").build();
        User u1 = User.builder().id(1L).tokens(List.of(token1)).build();
        User u2 = User.builder().id(1L).tokens(List.of(t)).build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void shouldToStringHandleNullFields() {
        User u = new User();
        String str = u.toString();
        assertThat(str).contains("User");
    }

    @Test
    void shouldUserDetailsMethodsHandleNullFields() {
        User emptyUser = new User();
        UserDetails userDetails = emptyUser;
        assertThat(userDetails.getAuthorities()).isEmpty();
        assertThat(userDetails.getUsername()).isNull();
        assertThat(userDetails.getPassword()).isNull();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void shouldEqualsAndHashCodeWithAllFieldsNull() {
        User u1 = new User();
        User u2 = new User();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        User u = new User();
        assertNotEquals(u, null);
        assertNotEquals(u, "string");
    }

    @Test
    void shouldNotBeEqualWhenOnlyOneFieldDiffers() {
        User u1 = User.builder().email("a@a.com").build();
        User u2 = User.builder().email("b@b.com").build();
        assertNotEquals(u1, u2);
    }

    @Test
    void shouldToStringWithNullFields() {
        User u = new User();
        String str = u.toString();
        assertNotNull(str);
        assertTrue(str.contains("User"));
    }
}