package br.edu.fatecsjc.lgnspringapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void register_ShouldCallServiceAndReturn201() throws Exception {
        // Arrange
        RegisterRequestDTO dto = new RegisterRequestDTO("John", "Doe", "john@example.com", "password", null);
        AuthenticationResponseDTO response = new AuthenticationResponseDTO("access_token", "refresh_token");

        when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));

        verify(authenticationService, times(1)).register(any(RegisterRequestDTO.class));
    }

    @Test
    @WithMockUser
    void authenticate_ShouldCallServiceAndReturn200() throws Exception {
        // Arrange
        AuthenticationRequestDTO dto = new AuthenticationRequestDTO("john@example.com", "password");
        AuthenticationResponseDTO response = new AuthenticationResponseDTO("access_token", "refresh_token");

        when(authenticationService.authenticate(any(AuthenticationRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));

        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequestDTO.class));
    }

    @Test
    @WithMockUser
    void refreshToken_ShouldCallServiceAndReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token"))
                .andExpect(status().isOk());

        verify(authenticationService, times(1))
                .refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}