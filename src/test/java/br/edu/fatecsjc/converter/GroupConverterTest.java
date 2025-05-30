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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
}
