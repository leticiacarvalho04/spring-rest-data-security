package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupConverterTest {

    @Mock
    private OrganizationRepository organizationRepository;

    private ModelMapper modelMapper;
    private GroupConverter groupConverter;

    private GroupDTO groupDTO;
    private Group group;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        groupConverter = new GroupConverter(modelMapper);
        // Injeta o mock manualmente, já que não está usando @InjectMocks
        Field repoField;
        try {
            repoField = GroupConverter.class.getDeclaredField("organizationRepository");
            repoField.setAccessible(true);
            repoField.set(groupConverter, organizationRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        groupDTO = new GroupDTO();
        groupDTO.setName("Grupo 1");

        MemberDTO member1 = new MemberDTO();
        member1.setId(1L);
        member1.setName("João");

        MemberDTO member2 = new MemberDTO();
        member2.setId(2L);
        member2.setName("Maria");

        groupDTO.setMembers(List.of(member1, member2));

        OrganizationDTO orgDTO = new OrganizationDTO();
        orgDTO.setId(10L);
        orgDTO.setName("Org Test");
        groupDTO.setOrganization(orgDTO);

        group = new Group();
        group.setId(1L);
        group.setName("Grupo 1");

        Member entityMember1 = new Member();
        entityMember1.setId(1L);
        entityMember1.setName("João");

        Member entityMember2 = new Member();
        entityMember2.setId(2L);
        entityMember2.setName("Maria");

        group.setMembers(List.of(entityMember1, entityMember2));
        Organization org = new Organization();
        org.setId(10L);
        org.setName("Org Test");
        group.setOrganization(org);
    }

    @Test
    void shouldConvertDtoToEntityAndSetMemberReferencesAndOrganization() {
        Organization org = new Organization();
        org.setId(10L);
        org.setName("Org Test");
        when(organizationRepository.findById(10L)).thenReturn(Optional.of(org));

        Group result = groupConverter.convertToEntity(groupDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Grupo 1");
        assertThat(result.getMembers()).hasSize(2);
        assertThat(result.getOrganization()).isNotNull();
        assertThat(result.getOrganization().getId()).isEqualTo(10L);

        result.getMembers().forEach(member -> {
            assertThat(member.getGroup()).isEqualTo(result);
        });
    }

    @Test
    void shouldSetGroupReferenceOnAllMembersWhenConvertingList() {
        Organization org = new Organization();
        org.setId(10L);

        List<Group> result = groupConverter.convertToEntity(List.of(groupDTO));

        assertThat(result).hasSize(1);
        Group resultGroup = result.get(0);
        assertThat(resultGroup.getMembers()).hasSize(2);

        resultGroup.getMembers().forEach(member -> {
            assertThat(member.getGroup()).isEqualTo(resultGroup);
        });
    }

    @Test
    void shouldUpdateExistingEntityWhenConvertToEntityWithExistingGroup() {
        Group existingGroup = new Group();
        existingGroup.setId(1L);
        existingGroup.setName("Old Name");

        GroupDTO dto = new GroupDTO();
        dto.setName("Updated Name");

        Group result = groupConverter.convertToEntity(dto, existingGroup);

        assertThat(result).isSameAs(existingGroup);
        assertThat(result.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldConvertEntityToDtoCorrectly() {
        GroupDTO result = groupConverter.convertToDto(group);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Grupo 1");
        assertThat(result.getMembers()).hasSize(2);
        assertThat(result.getMembers().get(0).getName()).isEqualTo("João");
    }

    @Test
    void shouldConvertListOfEntitiesToListOfDtos() {
        List<Group> groups = List.of(group);

        List<GroupDTO> result = groupConverter.convertToDto(groups);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Grupo 1");
        assertThat(result.get(0).getMembers()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenConvertingNullOrEmptyDtos() {
        List<Group> result = groupConverter.convertToEntity(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleGroupWithNullMembers() {
        GroupDTO dto = new GroupDTO();
        dto.setName("Group Sem Membros");

        Group entity = groupConverter.convertToEntity(dto);

        assertThat(entity.getMembers()).isNotNull().isEmpty();
    }

    @Test
    void shouldThrowWhenOrganizationNotFound() {
        GroupDTO dto = new GroupDTO();
        OrganizationDTO orgDTO = new OrganizationDTO();
        orgDTO.setId(999L);
        dto.setOrganization(orgDTO);

        when(organizationRepository.findById(999L)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> groupConverter.convertToEntity(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Organization not found");
    }

    @Test
    void testEnsureTypeMapConfigured_IsCached() throws Exception {
        Organization org = new Organization();
        org.setId(10L);
        when(organizationRepository.findById(10L)).thenReturn(Optional.of(org));
        
        groupConverter.convertToEntity(groupDTO);

        Field propertyMapperDtoField = GroupConverter.class.getDeclaredField("propertyMapperDto");
        propertyMapperDtoField.setAccessible(true);

        TypeMap<GroupDTO, Group> firstMap = (TypeMap<GroupDTO, Group>) propertyMapperDtoField.get(groupConverter);

        groupConverter.convertToEntity(groupDTO);
        TypeMap<GroupDTO, Group> secondMap = (TypeMap<GroupDTO, Group>) propertyMapperDtoField.get(groupConverter);

        assertThat(firstMap).isSameAs(secondMap);
    }
}