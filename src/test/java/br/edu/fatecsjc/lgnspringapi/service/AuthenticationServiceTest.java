package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.enums.Role;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUser() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setFirstname("Test");
        request.setLastname("User");
        request.setEmail("test@mail.com");
        request.setPassword("123");
        request.setRole(Role.USER);

        User savedUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@mail.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh");
        when(tokenRepository.save(any())).thenReturn(null);

        // Act
        AuthenticationResponseDTO response = authenticationService.register(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void shouldAuthenticateUser() {
        // Arrange
        AuthenticationRequestDTO request = new AuthenticationRequestDTO();
        request.setEmail("test@mail.com");
        request.setPassword("123");

        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@mail.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh");
        when(tokenRepository.findAllValidTokenByUser(anyLong())).thenReturn(new ArrayList<>());
        when(tokenRepository.save(any())).thenReturn(null);

        // Act
        AuthenticationResponseDTO response = authenticationService.authenticate(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void shouldRefreshToken() throws IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@mail.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername("refreshToken")).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("refreshToken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(tokenRepository.findAllValidTokenByUser(anyLong())).thenReturn(new ArrayList<>());
        when(tokenRepository.save(any())).thenReturn(null);

        // Act
        authenticationService.refreshToken(request, response);

        // Assert
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(jwtService, times(1)).extractUsername("refreshToken");
        verify(jwtService, times(1)).isTokenValid("refreshToken", user);
        verify(jwtService, times(1)).generateToken(user);
    }
}
