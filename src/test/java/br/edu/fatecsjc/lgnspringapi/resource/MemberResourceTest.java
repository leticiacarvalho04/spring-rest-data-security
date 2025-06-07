package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberResourceTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private MemberService memberService;

        @Autowired
        private ObjectMapper objectMapper;

        private MemberDTO validMemberDto;

        @Autowired
        private WebApplicationContext webApplicationContext;

        // --- GET ALL MEMBERS ---

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldReturnAllMembers_whenGetAllMembersIsCalled() throws Exception {
                List<MemberDTO> members = List.of(
                                MemberDTO.builder().id(1L).name("John").age(25).groupId(100L).build(),
                                MemberDTO.builder().id(2L).name("Jane").age(30).groupId(200L).build());

                when(memberService.getAll()).thenReturn(members);

                mockMvc.perform(get("/member"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("John"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getAll_ServiceReturnsEmptyList_ShouldReturnEmptyArray() throws Exception {
                when(memberService.getAll()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/member"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        void getAll_Unauthorized_ShouldReturn403() throws Exception {
                mockMvc.perform(get("/member"))
                                .andExpect(status().isForbidden());
        }

        // --- GET MEMBER BY ID ---

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldReturnMemberById_whenGetMemberByIdIsCalled() throws Exception {
                when(memberService.findById(1L)).thenReturn(validMemberDto);

                mockMvc.perform(get("/member/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("John"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getMemberById_ServiceThrowsException_ShouldReturn400() throws Exception {
                when(memberService.findById(anyLong()))
                                .thenThrow(new RuntimeException("Erro ao buscar"));

                mockMvc.perform(get("/member/999"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getMemberById_ServiceReturnsNull_ShouldReturn200ButEmptyBody() throws Exception {
                when(memberService.findById(anyLong())).thenReturn(null);

                mockMvc.perform(get("/member/999"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(""));
        }

        @Test
        void getById_Unauthorized_ShouldReturn403() throws Exception {
                mockMvc.perform(get("/member/1"))
                                .andExpect(status().isForbidden());
        }

        // --- REGISTER MEMBER ---

        @Test
        @WithMockUser(authorities = { "admin:create" })
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
        @WithMockUser(authorities = { "admin:create" })
        void register_NullRequestBody_ShouldReturn201_BecauseNoValidation() throws Exception {
                mockMvc.perform(post("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(authorities = { "admin:create" })
        void register_ServiceThrowsException_ShouldReturn400() throws Exception {
                MemberDTO request = MemberDTO.builder().name("John").age(25).groupId(100L).build();

                when(memberService.save(any(MemberDTO.class)))
                                .thenThrow(new RuntimeException("Erro ao salvar"));

                mockMvc.perform(post("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        // --- UPDATE MEMBER ---

        @Test
        @WithMockUser(authorities = { "admin:update" })
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
        @WithMockUser(authorities = { "admin:update" })
        void update_EmptyJson_ShouldCallServiceAndReturn201() throws Exception {
                MemberDTO response = MemberDTO.builder().build();
                when(memberService.save(eq(1L), any(MemberDTO.class))).thenReturn(response);

                mockMvc.perform(put("/member/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").doesNotExist());
        }

        @Test
        @WithMockUser(authorities = { "admin:update" })
        void update_InvalidId_ShouldReturn400() throws Exception {
                MemberDTO dto = MemberDTO.builder().name("New Name").age(30).groupId(100L).build();

                when(memberService.save(eq(-1L), any(MemberDTO.class)))
                                .thenThrow(new RuntimeException("ID inválido"));

                mockMvc.perform(put("/member/-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = { "admin:update" })
        void update_ServiceThrowsException_ShouldReturn400() throws Exception {
                MemberDTO dto = MemberDTO.builder().name("John Updated").age(26).groupId(100L).build();

                when(memberService.save(eq(1L), any(MemberDTO.class)))
                                .thenThrow(new RuntimeException("Erro ao atualizar"));

                mockMvc.perform(put("/member/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        // --- DELETE MEMBER ---

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldDeleteMember_whenDeleteIsCalledWithValidId() throws Exception {
                doNothing().when(memberService).delete(1L);

                mockMvc.perform(delete("/member/1"))
                                .andExpect(status().isNoContent());

                verify(memberService, times(1)).delete(1L);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void delete_MissingId_ShouldCallServiceAndReturn204() throws Exception {
                mockMvc.perform(delete("/member/999"))
                                .andExpect(status().isNoContent());

                verify(memberService, times(1)).delete(eq(999L));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void delete_ServiceThrowsException_ShouldReturn400() throws Exception {
                doThrow(new RuntimeException("Erro ao deletar")).when(memberService).delete(eq(1L));

                mockMvc.perform(delete("/member/1"))
                                .andExpect(status().isBadRequest());
        }
}