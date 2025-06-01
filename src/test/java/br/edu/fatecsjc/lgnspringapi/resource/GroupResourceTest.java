package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class GroupResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Test
    void shouldReturnAllGroups() throws Exception {
        when(groupService.getAll()).thenReturn(Collections.singletonList(new GroupDTO()));
        mockMvc.perform(get("/group"))
                .andExpect(status().isOk());
    }
}