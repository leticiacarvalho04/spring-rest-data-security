package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.enums.Role;
import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import io.jsonwebtoken.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@mail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldRegisterUser() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Test", "User", "test@mail.com", "password", Role.USER
        );

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponseDTO response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void shouldAuthenticateUser() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO(
                "test@mail.com", "password"
        );

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        when(tokenRepository.findAllValidTokenByUser(user.getId()))
                .thenReturn(Collections.emptyList());

        AuthenticationResponseDTO response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        "test@mail.com",
                        "password"
                )
        );

        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void shouldRefreshToken() throws Exception {
        String refreshToken = "refreshToken";
        String newAccessToken = "newAccessToken";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ServletOutputStream outputStream = new ServletOutputStream() {
            private ByteArrayOutputStream baos = new ByteArrayOutputStream();

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {}

            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public String toString() {
                return baos.toString();
            }
        };

        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(newAccessToken);

        when(tokenRepository.findAllValidTokenByUser(user.getId()))
                .thenReturn(Collections.emptyList());

        when(response.getOutputStream()).thenReturn(outputStream);

        authenticationService.refreshToken(request, response);

        String jsonResponse = outputStream.toString();

        assertTrue(jsonResponse.contains("accessToken"));
        assertTrue(jsonResponse.contains("refreshToken"));
        assertTrue(jsonResponse.contains(newAccessToken));
        assertTrue(jsonResponse.contains(refreshToken));

        verify(tokenRepository).save(any(Token.class));
    }
}
