package br.edu.fatecsjc.lgnspringapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.enums.Role;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationResourceTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthenticationService authenticationService;

        @Autowired
        private ObjectMapper objectMapper;

        private RegisterRequestDTO validRegisterDto;
        private AuthenticationRequestDTO validAuthDto;

        @BeforeEach
        void setUp() {
                validRegisterDto = new RegisterRequestDTO("John", "Doe", "john@example.com", "password", null);
                validAuthDto = new AuthenticationRequestDTO("john@example.com", "password");
        }

        // --- REGISTER ---

        @Test
        @WithMockUser
        void register_ShouldCallServiceAndReturn201() throws Exception {
                AuthenticationResponseDTO response = new AuthenticationResponseDTO("access_token", "refresh_token");

                when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRegisterDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.accessToken").value("access_token"))
                                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));

                verify(authenticationService, times(1)).register(any(RegisterRequestDTO.class));
        }

        @Test
        @WithMockUser
        void register_InvalidFields_DoesNotThrowBadRequest_BecauseNoValidation() throws Exception {
                RegisterRequestDTO invalidDto = new RegisterRequestDTO("", "", "", "", null); // Campos vazios

                AuthenticationResponseDTO fakeResponse = new AuthenticationResponseDTO("token", "refresh");

                when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(fakeResponse);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDto)))
                                .andExpect(status().isCreated());

                verify(authenticationService, times(1)).register(any(RegisterRequestDTO.class));
        }

        @Test
        @WithMockUser
        void register_ServiceThrowsException_ShouldReturn400_BecauseNoGlobalExceptionHandler() throws Exception {
                when(authenticationService.register(any(RegisterRequestDTO.class)))
                                .thenThrow(new RuntimeException("Erro ao registrar usuário"));

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRegisterDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void register_WithRole_ShouldCallService() throws Exception {
                RegisterRequestDTO dto = new RegisterRequestDTO("John", "Doe", "john@example.com", "password",
                                Role.ADMIN);

                AuthenticationResponseDTO response = new AuthenticationResponseDTO("token", "refresh");

                when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated());

                verify(authenticationService).register(dto);
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void register_ByAdminUser_ShouldCallService() throws Exception {
                RegisterRequestDTO dto = new RegisterRequestDTO("John", "Doe", "john@example.com", "password",
                                Role.USER);

                AuthenticationResponseDTO response = new AuthenticationResponseDTO("token", "refresh");

                when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated());

                verify(authenticationService).register(dto);
        }

        // --- AUTHENTICATE ---

        @Test
        @WithMockUser
        void authenticate_ShouldCallServiceAndReturn200() throws Exception {
                AuthenticationResponseDTO response = new AuthenticationResponseDTO("access_token", "refresh_token");

                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validAuthDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accessToken").value("access_token"))
                                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));

                verify(authenticationService, times(1)).authenticate(any(AuthenticationRequestDTO.class));
        }

        @Test
        @WithMockUser
        void authenticate_InvalidCredentials_Returns400_BecauseNoCustomHandling() throws Exception {
                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class)))
                                .thenThrow(new RuntimeException("Credenciais inválidas"));

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validAuthDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void authenticate_ServiceReturnsNull_ShouldReturn200ButEmptyBody() throws Exception {
                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(null);

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validAuthDto)))
                                .andExpect(status().isOk())
                                .andExpect(content().string(""));
        }

        @Test
        @WithMockUser
        void authenticate_PasswordIsNull_ShouldStillCallService() throws Exception {
                AuthenticationRequestDTO dto = new AuthenticationRequestDTO("john@example.com", null);

                AuthenticationResponseDTO response = new AuthenticationResponseDTO("token", "refresh");

                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk());

                verify(authenticationService).authenticate(dto);
        }

        @Test
        @WithMockUser
        void authenticate_EmailIsEmpty_ShouldCallServiceButFailInternally() throws Exception {
                AuthenticationRequestDTO dto = new AuthenticationRequestDTO("", "password");

                AuthenticationResponseDTO response = new AuthenticationResponseDTO("token", "refresh");

                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk());

                verify(authenticationService).authenticate(dto);
        }

        @Test
        @WithMockUser
        void authenticate_Fails_DoesNotCallRegister() throws Exception {
                when(authenticationService.authenticate(any(AuthenticationRequestDTO.class)))
                                .thenThrow(new RuntimeException("Erro"));

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validAuthDto)))
                                .andExpect(status().isBadRequest());

                verify(authenticationService, never()).register(any(RegisterRequestDTO.class));
        }

        @Test
        @WithMockUser
        void authenticate_MalformedJson_ShouldReturn400() throws Exception {
                String malformedJson = "{email: john@example.com, password:}";

                mockMvc.perform(post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
                                .andExpect(status().isBadRequest());
        }

        // --- REFRESH TOKEN ---

        @Test
        @WithMockUser
        void refreshToken_ShouldCallServiceAndReturn200() throws Exception {
                mockMvc.perform(post("/auth/refresh-token"))
                                .andExpect(status().isOk());

                verify(authenticationService, times(1))
                                .refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
        }

        @Test
        void refreshToken_Unauthorized_ShouldReturn403() throws Exception {
                mockMvc.perform(post("/auth/refresh-token"))
                                .andExpect(status().isForbidden());

                verify(authenticationService, never())
                                .refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
        }

        @Test
        @WithMockUser
        void refreshToken_ServiceThrowsException_Returns400_BecauseNoGlobalExceptionHandler() throws Exception {
                doThrow(new IOException("Erro ao atualizar token"))
                                .when(authenticationService)
                                .refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

                mockMvc.perform(post("/auth/refresh-token"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void refreshToken_NoHeadersManipulation_ShouldStillWork() throws Exception {
                doNothing().when(authenticationService).refreshToken(any(HttpServletRequest.class),
                                any(HttpServletResponse.class));

                mockMvc.perform(post("/auth/refresh-token"))
                                .andExpect(status().isOk());

                verify(authenticationService).refreshToken(any(HttpServletRequest.class),
                                any(HttpServletResponse.class));
        }
}