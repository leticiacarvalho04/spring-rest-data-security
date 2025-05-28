package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}