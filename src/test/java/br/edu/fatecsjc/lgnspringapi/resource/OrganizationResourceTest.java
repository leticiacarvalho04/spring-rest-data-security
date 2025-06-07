package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationResourceTest {

	@MockBean
    private OrganizationService organizationService; 

    @Autowired
    private MockMvc mockMvc;

    private OrganizationDTO mockOrganization;
    
    @Autowired
    private OrganizationResource organizationResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockOrganization = new OrganizationDTO();
        mockOrganization.setId(1L);
        mockOrganization.setName("Tech Corp");
        mockOrganization.setInstitutionName("A tech company focused on innovation.");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllOrganizations() throws Exception {
        List<OrganizationDTO> organizations = Arrays.asList(mockOrganization);

        when(organizationService.getAll()).thenReturn(organizations);

        mockMvc.perform(get("/organization"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].name").value("Tech Corp"))
               .andExpect(jsonPath("$[0].institutionName").value("A tech company focused on innovation."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnOrganizationById_whenGetOrganizationByIdIsCalled() {
        Long id = 1L;
        when(organizationService.findById(id)).thenReturn(mockOrganization);

        ResponseEntity<OrganizationDTO> response = organizationResource.getOrganizationById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrganization, response.getBody());
        verify(organizationService, times(1)).findById(id);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"admin:update"})
    void shouldUpdateOrganization_whenUpdateIsCalledWithValidIdAndData() {
        Long id = 1L;
        OrganizationDTO updatedOrg = new OrganizationDTO();
        updatedOrg.setId(1L);
        updatedOrg.setName("Tech Corp Updated");
        updatedOrg.setInstitutionName("Updated description.");

        when(organizationService.save(id, mockOrganization)).thenReturn(updatedOrg);

        ResponseEntity<OrganizationDTO> response = organizationResource.update(id, mockOrganization);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updatedOrg, response.getBody());
        verify(organizationService, times(1)).save(id, mockOrganization);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"admin:create"})
    void shouldCreateOrganization_whenRegisterIsCalledWithData() {
        when(organizationService.save(mockOrganization)).thenReturn(mockOrganization);

        ResponseEntity<OrganizationDTO> response = organizationResource.register(mockOrganization);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockOrganization, response.getBody());
        verify(organizationService, times(1)).save(mockOrganization);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteOrganization_whenDeleteIsCalledWithValidId() {
        Long id = 1L;

        ResponseEntity<Void> response = organizationResource.update(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(organizationService, times(1)).delete(id);
    }
}