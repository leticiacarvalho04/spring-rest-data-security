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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class MarathonsResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarathonsService marathonsService;

    private MarathonsDTO mockMarathon;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        mockMarathon = MarathonsDTO.builder()
                .id(1L)
                .identification("Spring Marathon")
                .weight(75.0)
                .score(100)
                .members(List.of())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"admin:create"})
    void shouldCreateMarathon_whenRegisterIsCalledWithData() throws Exception {
        when(marathonsService.save(any())).thenReturn(mockMarathon);

        mockMvc.perform(post("/marathons")
                        .contentType("application/json")
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
    @WithMockUser(authorities = {"admin:update"})
    void shouldUpdateMarathon_whenUpdateIsCalledWithValidIdAndData() throws Exception {
        MarathonsDTO updated = MarathonsDTO.builder()
                .id(1L)
                .identification("Updated Marathon")
                .weight(80.0)
                .score(200)
                .members(List.of())
                .build();

        when(marathonsService.save(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/marathons/1")
                        .contentType("application/json")
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
    @WithMockUser(authorities = {"admin:delete"})
    void shouldDeleteMarathon_whenDeleteIsCalledWithValidId() throws Exception {
        doNothing().when(marathonsService).delete(1L);

        mockMvc.perform(delete("/marathons/1"))
                .andExpect(status().isNoContent());

        verify(marathonsService, times(1)).delete(1L);
    }
    
    @Test
    @WithMockUser(authorities = {"admin:delete"})
    public void shouldDeleteSuccessfully_whenUserHasDeleteAuthority() throws Exception {
        mockMvc.perform(delete("/marathons/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "unauthorizedUser", authorities = {"user:read"}) 
    void shouldReturnForbidden_whenUserDoesNotHaveDeleteAuthority() throws Exception {
        doNothing().when(marathonsService).delete(1L);

        mockMvc.perform(delete("/marathons/1"))
            .andDo(print()) 
            .andExpect(status().isForbidden());
    }
}
