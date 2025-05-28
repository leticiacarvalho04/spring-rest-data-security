package br.edu.fatecsjc.lgnspringapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCurrentPasswordIsWrong_thenThrowsException() {
        // Arrange
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .currentPassword("wrong_password")
                .newPassword("newPass123")
                .confirmationPassword("newPass123")
                .build();

        br.edu.fatecsjc.lgnspringapi.entity.User user = new br.edu.fatecsjc.lgnspringapi.entity.User();
        user.setPassword("encoded_correct_password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, Collections.emptyList()
        );

        // Simula senha atual incorreta
        when(passwordEncoder.matches("wrong_password", user.getPassword()))
                .thenReturn(false);

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.changePassword(request, authentication))
                .withMessage("Wrong password");
    }

    @Test
    void whenNewPasswordsDoNotMatch_thenThrowsException() {
        // Arrange
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .currentPassword("correct_password")
                .newPassword("newPass123")
                .confirmationPassword("differentPass123")
                .build();

        br.edu.fatecsjc.lgnspringapi.entity.User user = new br.edu.fatecsjc.lgnspringapi.entity.User();
        user.setPassword("encoded_correct_password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, Collections.emptyList()
        );

        // Simula senha atual correta
        when(passwordEncoder.matches("correct_password", user.getPassword()))
                .thenReturn(true);

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.changePassword(request, authentication))
                .withMessage("Wrong password");

        verify(userRepository, never()).save(any(br.edu.fatecsjc.lgnspringapi.entity.User.class));
    }

    @Test
    void whenPasswordChangesSuccessfully_thenSavesNewPassword() {
        // Arrange
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .currentPassword("correct_password")
                .newPassword("newPass123")
                .confirmationPassword("newPass123")
                .build();

        br.edu.fatecsjc.lgnspringapi.entity.User user = new br.edu.fatecsjc.lgnspringapi.entity.User();
        user.setEmail("test@example.com");
        user.setPassword("encoded_correct_password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        when(passwordEncoder.matches("correct_password", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPass123")).thenReturn("encoded_newPass123");

        // Act
        userService.changePassword(request, authentication);

        // Assert
        verify(userRepository, times(1)).save(argThat(u ->
                u.getPassword().equals("encoded_newPass123")
        ));
    }
}