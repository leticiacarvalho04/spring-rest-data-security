package br.edu.fatecsjc.lgnspringapi.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PermissionTest {

    @Test
    void shouldHaveCorrectValues() {
        assertEquals("admin:create", Permission.ADMIN_CREATE.getPermission());
        assertEquals("admin:update", Permission.ADMIN_UPDATE.getPermission());
        assertEquals("admin:delete", Permission.ADMIN_DELETE.getPermission());
        assertEquals("admin:read", Permission.ADMIN_READ.getPermission());
    }

    @Test
    void shouldReturnCorrectNameAndOrdinal() {
        assertEquals("ADMIN_CREATE", Permission.ADMIN_CREATE.name());
        assertEquals("ADMIN_UPDATE", Permission.ADMIN_UPDATE.name());
        assertEquals("ADMIN_DELETE", Permission.ADMIN_DELETE.name());
        assertEquals("ADMIN_READ", Permission.ADMIN_READ.name());

        assertEquals(0, Permission.ADMIN_CREATE.ordinal());
        assertEquals(1, Permission.ADMIN_UPDATE.ordinal());
        assertEquals(2, Permission.ADMIN_DELETE.ordinal());
        assertEquals(3, Permission.ADMIN_READ.ordinal());
    }

    @Test
    void shouldReturnAllEnumValues() {
        Permission[] permissions = Permission.values();

        assertNotNull(permissions);
        assertEquals(4, permissions.length);
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_CREATE));
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_UPDATE));
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_DELETE));
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_READ));
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = Permission.ADMIN_CREATE.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ADMIN_CREATE"));
    }
}