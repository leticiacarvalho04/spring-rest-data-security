package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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
        when(organizationConverter.convertToDto(anyList()))
                .thenReturn(Collections.singletonList(new OrganizationDTO()));
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
    void shouldReturnNullWhenOrganizationNotFoundById() {
        when(organizationRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(organizationService.findById(2L));
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

    @Test
    void shouldSaveOrganizationWithIdWhenExistsAndWithGroups() {
        Long id = 1L;
        OrganizationDTO dto = new OrganizationDTO();
        Organization existing = new Organization();
        Group group = new Group();
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        existing.setGroups(groups);

        Organization orgToSave = new Organization();
        orgToSave.setGroups(groups);

        Organization orgReturned = new Organization();
        when(organizationRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(groupRepository).deleteAllByOrganization(existing);
        when(organizationConverter.convertToEntity(dto, existing)).thenReturn(orgToSave);
        when(organizationRepository.save(orgToSave)).thenReturn(orgReturned);
        when(organizationConverter.convertToDto(orgReturned)).thenReturn(dto);

        OrganizationDTO result = organizationService.save(id, dto);
        assertNotNull(result);
        verify(groupRepository).deleteAllByOrganization(existing);
        verify(organizationRepository).save(orgToSave);
    }

    @Test
    void shouldSaveOrganizationWithIdWhenExistsAndWithEmptyGroups() {
        Long id = 2L;
        OrganizationDTO dto = new OrganizationDTO();
        Organization existing = new Organization();
        existing.setGroups(new ArrayList<>()); // lista vazia

        Organization orgToSave = new Organization();
        orgToSave.setGroups(new ArrayList<>());

        Organization orgReturned = new Organization();
        when(organizationRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(groupRepository).deleteAllByOrganization(existing);
        when(organizationConverter.convertToEntity(dto, existing)).thenReturn(orgToSave);
        when(organizationRepository.save(orgToSave)).thenReturn(orgReturned);
        when(organizationConverter.convertToDto(orgReturned)).thenReturn(dto);

        OrganizationDTO result = organizationService.save(id, dto);
        assertNotNull(result);
        verify(groupRepository).deleteAllByOrganization(existing);
        verify(organizationRepository).save(orgToSave);
    }
    
    @Test
    void shouldReturnNullWhenSaveOrganizationWithIdNotFound() {
        Long id = 4L;
        OrganizationDTO dto = new OrganizationDTO();
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(organizationService.save(id, dto));
    }
}