package br.edu.fatecsjc.lgnspringapi.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleTest {

    @Test
    void shouldHaveValidValues() {
        Role user = Role.USER;
        Role admin = Role.ADMIN;

        assertNotNull(user.getPermissions());
        assertTrue(user.getPermissions().isEmpty());

        assertNotNull(admin.getPermissions());
        assertEquals(Set.of(Permission.ADMIN_CREATE, Permission.ADMIN_UPDATE), admin.getPermissions());
    }

    @Test
    void shouldGetAuthoritiesForUser() {
        List<SimpleGrantedAuthority> authorities = Role.USER.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void shouldGetAuthoritiesForAdmin() {
        List<SimpleGrantedAuthority> authorities = Role.ADMIN.getAuthorities();

        assertNotNull(authorities);
        assertEquals(3, authorities.size()); // ROLE_ADMIN + 2 permissões
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("admin:create")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("admin:update")));
    }

    @Test
    void shouldReturnAllEnumValues() {
        Role[] roles = Role.values();

        assertNotNull(roles);
        assertEquals(2, roles.length);
        assertTrue(java.util.Arrays.asList(roles).contains(Role.USER));
        assertTrue(java.util.Arrays.asList(roles).contains(Role.ADMIN));
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = Role.ADMIN.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ADMIN"));
    }
}