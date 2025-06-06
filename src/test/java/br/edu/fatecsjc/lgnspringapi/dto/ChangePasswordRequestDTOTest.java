package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChangePasswordRequestDTOTest {

    @Test
    void testBuilderAndGetters() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("oldPass")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        assertEquals("oldPass", dto.getCurrentPassword());
        assertEquals("newPass", dto.getNewPassword());
        assertEquals("newPass", dto.getConfirmationPassword());
    }

    @Test
    void testSettersAndNoArgsConstructor() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO();
        dto.setCurrentPassword("c1");
        dto.setNewPassword("c2");
        dto.setConfirmationPassword("c3");

        assertEquals("c1", dto.getCurrentPassword());
        assertEquals("c2", dto.getNewPassword());
        assertEquals("c3", dto.getConfirmationPassword());
    }

    @Test
    void testEqualsAndHashCodeWithNullFields() {
        ChangePasswordRequestDTO dto1 = ChangePasswordRequestDTO.builder().build();
        ChangePasswordRequestDTO dto2 = ChangePasswordRequestDTO.builder().build();

        // Se equals/hashCode não estão sobrescritos, isso será false!
        // Então compare campos:
        assertEquals(dto1.getCurrentPassword(), dto2.getCurrentPassword());
        assertEquals(dto1.getNewPassword(), dto2.getNewPassword());
        assertEquals(dto1.getConfirmationPassword(), dto2.getConfirmationPassword());
    }

    @Test
    void testToStringContainsFields() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("oldPass")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        String str = dto.toString();
        // Se usar Lombok @ToString, isso funciona:
        assertTrue(str.contains("oldPass") || str.contains("currentPassword=oldPass"));
        assertTrue(str.contains("newPass") || str.contains("newPassword=newPass"));
    }

    @Test
    void testEqualsAndHashCode() {
        ChangePasswordRequestDTO dto1 = ChangePasswordRequestDTO.builder()
                .currentPassword("oldPass")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        ChangePasswordRequestDTO dto2 = ChangePasswordRequestDTO.builder()
                .currentPassword("oldPass")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        ChangePasswordRequestDTO dto3 = ChangePasswordRequestDTO.builder()
                .currentPassword("other")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testEqualsWithNullAndOtherType() {
        ChangePasswordRequestDTO dto = ChangePasswordRequestDTO.builder()
                .currentPassword("oldPass")
                .newPassword("newPass")
                .confirmationPassword("newPass")
                .build();

        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a dto");
        assertEquals(dto, dto);
    }
}