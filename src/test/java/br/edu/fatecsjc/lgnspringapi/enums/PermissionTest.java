package br.edu.fatecsjc.lgnspringapi.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PermissionTest {

    @Test
    void shouldHaveCorrectValues() {
        Permission adminCreate = Permission.ADMIN_CREATE;
        Permission adminUpdate = Permission.ADMIN_UPDATE;

        assertEquals("admin:create", adminCreate.getPermission());
        assertEquals("admin:update", adminUpdate.getPermission());
    }

    @Test
    void shouldReturnCorrectNameAndOrdinal() {
        assertEquals("ADMIN_CREATE", Permission.ADMIN_CREATE.name());
        assertEquals(0, Permission.ADMIN_CREATE.ordinal());

        assertEquals("ADMIN_UPDATE", Permission.ADMIN_UPDATE.name());
        assertEquals(1, Permission.ADMIN_UPDATE.ordinal());
    }

    @Test
    void shouldReturnAllEnumValues() {
        Permission[] permissions = Permission.values();

        assertNotNull(permissions);
        assertEquals(2, permissions.length);
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_CREATE));
        assertTrue(java.util.Arrays.asList(permissions).contains(Permission.ADMIN_UPDATE));
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = Permission.ADMIN_CREATE.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ADMIN_CREATE"));
    }
}