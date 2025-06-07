package br.edu.fatecsjc.lgnspringapi.dto;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestDTOTest {

    private RegisterRequestDTO registerRequestDTO;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setFirstname("John");
        registerRequestDTO.setLastname("Doe");
        registerRequestDTO.setEmail("john.doe@example.com");
        registerRequestDTO.setPassword("password123");
        registerRequestDTO.setRole(Role.ADMIN);
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals("John", registerRequestDTO.getFirstname());
        assertEquals("Doe", registerRequestDTO.getLastname());
        assertEquals("john.doe@example.com", registerRequestDTO.getEmail());
        assertEquals("password123", registerRequestDTO.getPassword());
        assertEquals(Role.ADMIN, registerRequestDTO.getRole());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        RegisterRequestDTO dto = new RegisterRequestDTO();

        assertNull(dto.getFirstname());
        assertNull(dto.getLastname());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
        assertNull(dto.getRole());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Jane", "Smith", "jane.smith@example.com", "secret", Role.USER);

        assertEquals("Jane", dto.getFirstname());
        assertEquals("Smith", dto.getLastname());
        assertEquals("jane.smith@example.com", dto.getEmail());
        assertEquals("secret", dto.getPassword());
        assertEquals(Role.USER, dto.getRole());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("Built")
                .lastname("User")
                .email("built.user@example.com")
                .password("builtSecret")
                .role(Role.ADMIN)
                .build();

        assertEquals("Built", dto.getFirstname());
        assertEquals("User", dto.getLastname());
        assertEquals("built.user@example.com", dto.getEmail());
        assertEquals("builtSecret", dto.getPassword());
        assertEquals(Role.ADMIN, dto.getRole());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john.doe@example.com"));
        assertTrue(toString.contains("ADMIN"));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        RegisterRequestDTO differentDTO = RegisterRequestDTO.builder()
                .firstname("Jane")
                .lastname("Smith")
                .email("jane.smith@example.com")
                .password("differentPassword")
                .role(Role.USER)
                .build();

        // Test equals
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, differentDTO);

        // Test hash code
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), differentDTO.hashCode());
    }

    @Test
    void shouldNotBeEqualToNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        assertFalse(dto.equals(null));
    }

    @Test
    void shouldNotBeEqualToDifferentObjectType() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        Object other = new Object();
        assertFalse(dto.equals(other));
    }

    @Test
    void shouldEqualsHandleNullFieldsCorrectly() {
        RegisterRequestDTO dto1 = new RegisterRequestDTO();
        RegisterRequestDTO dto2 = new RegisterRequestDTO();

        assertTrue(dto1.equals(dto2));
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOneFieldDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("wrongPassword")
                .role(Role.ADMIN)
                .build();

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldGenerateToStringWithNullFields() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("RegisterRequestDTO"));
    }

    @Test
    void shouldHashCodeBeConsistent() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        int firstHash = dto.hashCode();
        int secondHash = dto.hashCode();

        assertEquals(firstHash, secondHash);
    }

    @Test
    void shouldEqualsAndHashCodeWithAllFieldsNull() {
        RegisterRequestDTO dto1 = new RegisterRequestDTO();
        RegisterRequestDTO dto2 = new RegisterRequestDTO();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "string");
    }

    @Test
    void shouldNotBeEqualWhenOnlyOneFieldDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder().firstname("A").build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder().firstname("B").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldToStringWithNullFields() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("RegisterRequestDTO"));
    }

    @Test
    void shouldBeReflexive() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        assertEquals(dto, dto);
    }

    @Test
    void shouldNotBeEqualWhenOnlyFirstnameDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("A")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("B")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyLastnameDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("A")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("B")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyEmailDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("a@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("b@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyRoleDiffers() {
        RegisterRequestDTO dto1 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        RegisterRequestDTO dto2 = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.USER)
                .build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldAllArgsConstructorWithNulls() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .firstname(null)
                .lastname(null)
                .email(null)
                .password(null)
                .role(null)
                .build();
        assertNull(dto.getFirstname());
        assertNull(dto.getLastname());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
        assertNull(dto.getRole());
    }
}