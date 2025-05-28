package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.config.JwtAuthenticationFilter;
import br.edu.fatecsjc.lgnspringapi.config.SecurityConfig;
import br.edu.fatecsjc.lgnspringapi.config.TestSecurityConfig;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMIN"}) // Valido pra todos os testes
class MemberResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllMembers_whenGetAllMembersIsCalled() throws Exception {
        List<MemberDTO> members = List.of(
                MemberDTO.builder().id(1L).name("John").age(25).groupId(100L).build(),
                MemberDTO.builder().id(2L).name("Jane").age(30).groupId(200L).build()
        );

        when(memberService.getAll()).thenReturn(members);

        mockMvc.perform(get("/member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane"));
    }

    @Test
    void shouldReturnMemberById_whenGetMemberByIdIsCalled() throws Exception {
        MemberDTO member = MemberDTO.builder().id(1L).name("John").age(25).groupId(100L).build();

        when(memberService.findById(1L)).thenReturn(member);

        mockMvc.perform(get("/member/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    @WithMockUser(authorities = {"admin:update"})
    void shouldUpdateMember_whenUpdateIsCalledWithValidIdAndData() throws Exception {
        MemberDTO request = MemberDTO.builder().name("John Updated").age(26).groupId(100L).build();
        MemberDTO response = MemberDTO.builder().id(1L).name("John Updated").age(26).groupId(100L).build();

        when(memberService.save(eq(1L), any(MemberDTO.class))).thenReturn(response);

        mockMvc.perform(put("/member/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    @WithMockUser(authorities = {"admin:create"})
    void shouldCreateMember_whenRegisterIsCalledWithData() throws Exception {
        MemberDTO request = MemberDTO.builder().name("John").age(25).groupId(100L).build();
        MemberDTO response = MemberDTO.builder().id(1L).name("John").age(25).groupId(100L).build();

        when(memberService.save(any(MemberDTO.class))).thenReturn(response);

        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldDeleteMember_whenDeleteIsCalledWithValidId() throws Exception {
        doNothing().when(memberService).delete(1L);

        mockMvc.perform(delete("/member/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldThrowAccessDenied_whenUserDoesNotHaveRequiredAuthority() throws Exception {
        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                MemberDTO.builder().name("John").age(25).groupId(100L).build()
                        )))
                .andExpect(status().isForbidden());
    }
}
