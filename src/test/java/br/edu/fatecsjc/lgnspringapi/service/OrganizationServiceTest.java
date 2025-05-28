package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private OrganizationConverter organizationConverter;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllOrganizations() {
        when(organizationRepository.findAll()).thenReturn(Collections.singletonList(new Organization()));
        when(organizationConverter.convertToDto(anyList())).thenReturn(Collections.singletonList(new OrganizationDTO()));
        assertEquals(1, organizationService.getAll().size());
    }

    @Test
    void shouldReturnOrganizationById() {
        Organization org = new Organization();
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizationConverter.convertToDto(org)).thenReturn(new OrganizationDTO());
        assertNotNull(organizationService.findById(1L));
    }

    @Test
    void shouldSaveOrganization() {
        OrganizationDTO dto = new OrganizationDTO();
        Organization org = new Organization();
        when(organizationConverter.convertToEntity(dto)).thenReturn(org);
        when(organizationRepository.save(org)).thenReturn(org);
        when(organizationConverter.convertToDto(org)).thenReturn(dto);
        assertNotNull(organizationService.save(dto));
    }

    @Test
    void shouldDeleteOrganization() {
        organizationService.delete(1L);
        verify(organizationRepository, times(1)).deleteById(1L);
    }
}