package br.edu.fatecsjc.converter;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class GroupConverterTest {

    private ModelMapper modelMapper;
    private GroupConverter groupConverter;

    private GroupDTO groupDTO;
    private Group group;

    @BeforeEach
    void setUp() {
        // Instanciando o ModelMapper real
        modelMapper = new ModelMapper();
        groupConverter = new GroupConverter(modelMapper);

        // Criando DTO
        groupDTO = new GroupDTO();
        groupDTO.setName("Grupo 1");

        MemberDTO member1 = new MemberDTO();
        member1.setId(1L);
        member1.setName("João");

        MemberDTO member2 = new MemberDTO();
        member2.setId(2L);
        member2.setName("Maria");

        groupDTO.setMembers(List.of(member1, member2));

        // Criando Entidade
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
    }

    @Test
    void shouldConvertDtoToEntityAndSetMemberReferences() {
        // Act
        Group result = groupConverter.convertToEntity(groupDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Grupo 1");
        assertThat(result.getMembers()).hasSize(2);

        result.getMembers().forEach(member -> {
            assertThat(member.getGroup()).isEqualTo(result);
        });
    }

    @Test
    void shouldSetGroupReferenceOnAllMembersWhenConvertingList() {
        // Act
        List<Group> result = groupConverter.convertToEntity(List.of(groupDTO));

        // Assert
        assertThat(result).hasSize(1);
        Group resultGroup = result.get(0);
        assertThat(resultGroup.getMembers()).hasSize(2);

        resultGroup.getMembers().forEach(member -> {
            assertThat(member.getGroup()).isEqualTo(resultGroup);
        });
    }
    
    @Test
    void shouldUpdateExistingEntityWhenConvertToEntityWithExistingGroup() {
        // Arrange
        Group existingGroup = new Group();
        existingGroup.setId(1L);
        existingGroup.setName("Old Name");

        GroupDTO dto = new GroupDTO();
        dto.setName("Updated Name");

        // Act
        Group result = groupConverter.convertToEntity(dto, existingGroup);

        // Assert
        assertThat(result).isSameAs(existingGroup);
        assertThat(result.getName()).isEqualTo("Updated Name");
    }
    
    @Test
    void shouldConvertEntityToDtoCorrectly() {
        // Act
        GroupDTO result = groupConverter.convertToDto(group);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Grupo 1");
        assertThat(result.getMembers()).hasSize(2);
        assertThat(result.getMembers().get(0).getName()).isEqualTo("João");
    }
    
    @Test
    void shouldConvertListOfEntitiesToListOfDtos() {
        // Arrange
        List<Group> groups = List.of(group);

        // Act
        List<GroupDTO> result = groupConverter.convertToDto(groups);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Grupo 1");
        assertThat(result.get(0).getMembers()).hasSize(2);
    }
    
    @Test
    void shouldReturnEmptyListWhenConvertingNullOrEmptyDtos() {
        // Act
        List<Group> result = groupConverter.convertToEntity(List.of());

        // Assert
        assertThat(result).isEmpty();
    }
    
    @Test
    void shouldHandleGroupWithNullMembers() {
        // Arrange
        GroupDTO dto = new GroupDTO();
        dto.setName("Group Sem Membros");

        // Act
        Group entity = groupConverter.convertToEntity(dto);

        // Assert
        assertThat(entity.getMembers()).isNotNull().isEmpty();
    }
    
    @Test
    void testEnsureTypeMapConfigured_IsCached() throws Exception {
        groupConverter.convertToEntity(groupDTO);

        Field propertyMapperDtoField = GroupConverter.class.getDeclaredField("propertyMapperDto");
        propertyMapperDtoField.setAccessible(true);

        TypeMap<GroupDTO, Group> firstMap = (TypeMap<GroupDTO, Group>) propertyMapperDtoField.get(groupConverter);

        groupConverter.convertToEntity(groupDTO);
        TypeMap<GroupDTO, Group> secondMap = (TypeMap<GroupDTO, Group>) propertyMapperDtoField.get(groupConverter);

        assertThat(firstMap).isSameAs(secondMap);
    }
}
