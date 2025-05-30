package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Test
    void testLogout_InvalidHeader_DoesNothing() {
        when(request.getHeader("Authorization")).thenReturn(null);
        logoutService.logout(request, response, authentication);
        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testLogout_MissingBearerPrefix_DoesNothing() {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");
        logoutService.logout(request, response, authentication);
        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testLogout_ValidToken_RevokesAndExpiresToken() {
        String jwt = "valid.jwt.token";
        Token token = new Token();
        token.setToken(jwt);
        token.setExpired(false);
        token.setRevoked(false);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).save(token);
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
    }

    @Test
    void testLogout_TokenNotFound_DoesNothing() {
        String jwt = "unknown.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByToken(jwt);
        verify(tokenRepository, never()).save(any(Token.class));
    }
}