package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.config.TestSecurityConfig;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarathonsResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarathonsService marathonsService;

    @Autowired
    private ObjectMapper objectMapper;

    private MarathonsDTO mockMarathon;

    @BeforeEach
    void setup() {
        mockMarathon = MarathonsDTO.builder()
                .id(1L)
                .identification("Spring Marathon")
                .weight(75.0)
                .score(100)
                .members(Collections.emptyList())
                .build();
    }

    // --- POST /marathons (Register) ---

    @Test
    @WithMockUser(authorities = {"admin:create"})
    void shouldCreateMarathon_whenRegisterIsCalledWithData() throws Exception {
        when(marathonsService.save(any())).thenReturn(mockMarathon);

        mockMvc.perform(post("/marathons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identification":"Spring Marathon",
                                  "weight":75.0,
                                  "score":100,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identification").value("Spring Marathon"))
                .andExpect(jsonPath("$.weight").value(75.0))
                .andExpect(jsonPath("$.score").value(100));
    }

    @Test
    @WithMockUser(authorities = {"admin:create"})
    void register_NullRequestBody_ShouldReturn201_BecauseNoValidation() throws Exception {
        mockMvc.perform(post("/marathons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"admin:create"})
    void register_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(marathonsService.save(any(MarathonsDTO.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        mockMvc.perform(post("/marathons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identification":"Spring Marathon",
                                  "weight":75.0,
                                  "score":100,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(post("/marathons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identification":"Spring Marathon",
                                  "weight":75.0,
                                  "score":100,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    // --- GET /marathons (getAll) ---

    @Test
    @WithMockUser(authorities = {"admin:read"})
    void shouldReturnAllMarathons_whenGetAllMarathonsIsCalled() throws Exception {
        when(marathonsService.getAll()).thenReturn(List.of(mockMarathon));

        mockMvc.perform(get("/marathons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].identification").exists())
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[0].members").isArray());
    }

    @Test
    @WithMockUser(authorities = {"admin:read"})
    void getAll_EmptyListFromService_ShouldReturnEmptyArray() throws Exception {
        when(marathonsService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/marathons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // --- GET /marathons/{id} (findById) ---

    @Test
    @WithMockUser(authorities = {"admin:read"})
    void shouldReturnMarathonById_whenGetMarathonByIdIsCalled() throws Exception {
        when(marathonsService.findById(1L)).thenReturn(mockMarathon);

        mockMvc.perform(get("/marathons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.identification").exists())
                .andExpect(jsonPath("$.weight").value(75.0))
                .andExpect(jsonPath("$.score").value(100))
                .andExpect(jsonPath("$.members").isArray());

        verify(marathonsService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(authorities = {"admin:read"})
    void getMarathonById_ServiceReturnsNull_ShouldReturn200ButEmptyBody() throws Exception {
        when(marathonsService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/marathons/999"))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(authorities = {"admin:read"})
    void getMarathonById_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(marathonsService.findById(anyLong()))
                .thenThrow(new RuntimeException("Erro ao buscar"));

        mockMvc.perform(get("/marathons/999"))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /marathons/{id} (update) ---

    @Test
    @WithMockUser(authorities = {"admin:update"})
    void shouldUpdateMarathon_whenUpdateIsCalledWithValidIdAndData() throws Exception {
        MarathonsDTO updated = MarathonsDTO.builder()
                .id(1L)
                .identification("Updated Marathon")
                .weight(80.0)
                .score(200)
                .members(Collections.emptyList())
                .build();

        when(marathonsService.save(eq(1L), any(MarathonsDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":1,
                                  "identification":"Updated Marathon",
                                  "weight":80.0,
                                  "score":200,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identification").value("Updated Marathon"))
                .andExpect(jsonPath("$.weight").value(80.0))
                .andExpect(jsonPath("$.score").value(200));
    }

    @Test
    @WithMockUser(authorities = {"admin:update"})
    void update_EmptyJson_ShouldCallServiceAndReturn201() throws Exception {
        MarathonsDTO response = MarathonsDTO.builder().build();
        when(marathonsService.save(eq(1L), any(MarathonsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    @WithMockUser(authorities = {"admin:update"})
    void update_InvalidId_ShouldReturn400() throws Exception {
        when(marathonsService.save(eq(-1L), any(MarathonsDTO.class)))
                .thenThrow(new RuntimeException("ID inválido"));

        mockMvc.perform(put("/marathons/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identification":"Invalid ID",
                                  "weight":80.0,
                                  "score":200,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"admin:update"})
    void update_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(marathonsService.save(eq(1L), any(MarathonsDTO.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar"));

        mockMvc.perform(put("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identification":"Error Update",
                                  "weight":80.0,
                                  "score":200,
                                  "members":[]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /marathons/{id} ---

    @Test
    @WithMockUser(authorities = {"admin:delete"})
    void deleteGroup_ShouldCallServiceAndReturn204() throws Exception {
        mockMvc.perform(delete("/marathons/1"))
                .andExpect(status().isNoContent());

        verify(marathonsService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(authorities = {"admin:delete"})
    void delete_MissingId_ShouldCallServiceAndReturn204() throws Exception {
        mockMvc.perform(delete("/marathons/999"))
                .andExpect(status().isNoContent());

        verify(marathonsService, times(1)).delete(eq(999L));
    }

    @Test
    @WithMockUser(authorities = {"admin:delete"})
    void delete_ServiceThrowsException_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Erro ao deletar grupo")).when(marathonsService).delete(eq(1L));
        mockMvc.perform(delete("/marathons/1"))
                .andExpect(status().isBadRequest());
    }
}