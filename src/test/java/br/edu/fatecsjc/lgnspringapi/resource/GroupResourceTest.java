package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GroupDTO validGroupDto;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        validGroupDto = GroupDTO.builder()
                .id(1L)
                .name("Developers")
                .build();
    }

    // --- GET ALL GROUPS ---
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllGroups_ShouldReturn200_WhenGroupsExist() throws Exception {
        when(groupService.getAll()).thenReturn(Collections.singletonList(validGroupDto));
        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllGroups_ShouldReturnEmptyList_WhenNoGroups() throws Exception {
        when(groupService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void getAllGroups_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/group"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllGroups_UserWithoutAdminRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/group"))
                .andExpect(status().isForbidden());
    }

    // --- GET GROUP BY ID ---
    @Test
    @WithMockUser(roles = "ADMIN")
    void getGroupById_ShouldReturn200_WhenExists() throws Exception {
        when(groupService.findById(anyLong())).thenReturn(validGroupDto);
        mockMvc.perform(get("/group/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Developers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGroupById_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(groupService.findById(anyLong())).thenThrow(new RuntimeException("Erro ao buscar grupo"));
        mockMvc.perform(get("/group/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled("")
    void getGroupById_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/group/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGroupById_ServiceReturnsNull_ShouldReturn200ButEmptyBody() throws Exception {
        when(groupService.findById(anyLong())).thenReturn(null);
        mockMvc.perform(get("/group/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyOrNullString()));
    }

    // --- REGISTER GROUP ---
    @Test
    @WithMockUser(authorities = "admin:create")
    void registerGroup_ShouldCallServiceAndReturn201() throws Exception {
        when(groupService.save(any(GroupDTO.class))).thenReturn(validGroupDto);
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Developers"));
        verify(groupService, times(1)).save(any(GroupDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Disabled("")
    void registerGroup_ForbiddenDueToMissingAuthority_ShouldReturn403() throws Exception {
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:create")
    void registerGroup_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(groupService.save(any(GroupDTO.class))).thenThrow(new RuntimeException("Erro ao salvar"));
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:create")
    void registerGroup_EmptyDTO_ShouldStillCallService() throws Exception {
        GroupDTO emptyDto = new GroupDTO();
        when(groupService.save(any(GroupDTO.class))).thenReturn(emptyDto);
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").doesNotExist());
        verify(groupService).save(emptyDto);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:create")
    void registerGroup_MalformedJson_ShouldReturn400() throws Exception {
        String malformedJson = "{name: \"Developers\"";
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:create")
    void registerGroup_NullBody_ShouldCallServiceButFail() throws Exception {
        when(groupService.save(any(GroupDTO.class))).thenThrow(new RuntimeException("Corpo nulo"));
        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    // --- UPDATE GROUP ---
    @Test
    @WithMockUser(username = "admin", authorities = "admin:update")
    void updateGroup_ShouldCallServiceAndReturn201() throws Exception {
        when(groupService.save(anyLong(), any(GroupDTO.class))).thenReturn(validGroupDto);
        mockMvc.perform(put("/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Developers"));
        verify(groupService, times(1)).save(eq(1L), any(GroupDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Disabled("")
    void updateGroup_ForbiddenDueToMissingAuthority_ShouldReturn403() throws Exception {
        mockMvc.perform(put("/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:update")
    void updateGroup_ServiceThrowsException_ShouldReturn400() throws Exception {
        when(groupService.save(anyLong(), any(GroupDTO.class))).thenThrow(new RuntimeException("Erro ao atualizar"));
        mockMvc.perform(put("/group/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:update")
    void updateGroup_InvalidId_ShouldCallServiceButFailInternally() throws Exception {
        GroupDTO dto = GroupDTO.builder().name("New Name").build();
        when(groupService.save(eq(-1L), any(GroupDTO.class))).thenThrow(new RuntimeException("ID inválido"));
        mockMvc.perform(put("/group/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:update")
    void updateGroup_EmptyJson_ShouldCallServiceAndReturn201() throws Exception {
        GroupDTO response = GroupDTO.builder().build();
        when(groupService.save(eq(1L), any(GroupDTO.class))).thenReturn(response);
        mockMvc.perform(put("/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").doesNotExist());
        verify(groupService).save(eq(1L), any(GroupDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin:update")
    void updateGroup_ServiceReturnsNull_ShouldReturn201ButEmptyBody() throws Exception {
        when(groupService.save(eq(1L), any(GroupDTO.class))).thenReturn(null);
        mockMvc.perform(put("/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validGroupDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    // --- DELETE GROUP ---
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_ShouldCallServiceAndReturn204() throws Exception {
        mockMvc.perform(delete("/group/1"))
                .andExpect(status().isNoContent());
        verify(groupService, times(1)).delete(eq(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_ServiceThrowsException_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Erro ao deletar grupo")).when(groupService).delete(eq(1L));
        mockMvc.perform(delete("/group/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_WhenServiceFails_ShouldNotCallDeleteAgain() throws Exception {
        doThrow(new RuntimeException("Erro interno")).when(groupService).delete(eq(1L));
        mockMvc.perform(delete("/group/1"))
                .andExpect(status().isBadRequest());
        verify(groupService, times(1)).delete(eq(1L));
    }
}