package br.edu.fatecsjc.converter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;

import java.util.List;
import java.util.Optional;

public class MemberConverterTest {

    private MemberConverter memberConverter;

    @BeforeEach
    void setUp() {
    	memberConverter = new MemberConverter(new ModelMapper());
    }

    @Test
    void testConvertToEntity() {
        // Arrange
        MarathonsDTO marathonDTO = MarathonsDTO.builder()
                .id(1L)
                .identification("Marathon 1")
                .build();

        MemberDTO dto = MemberDTO.builder()
                .id(100L) // Deve ser ignorado no mapeamento
                .name("John Doe")
                .groupId(200L)
                .marathons(List.of(marathonDTO))
                .build();

        // Act
        Member entity = memberConverter.convertToEntity(dto);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId(), "ID deve ser null, pois é ignorado no mapeamento.");
        assertEquals("John Doe", entity.getName());
        assertNotNull(entity.getGroup());
        assertEquals(200L, entity.getGroup().getId());

        assertNotNull(entity.getMarathons());
        assertEquals(1, entity.getMarathons().size());
        assertEquals("Marathon 1", entity.getMarathons().get(0).getIdentification());

        // Verificar relacionamento bidirecional
        assertNotNull(entity.getMarathons().get(0).getMembers());
        assertTrue(entity.getMarathons().get(0).getMembers().contains(entity));
    }

    public MemberDTO convertToDto(Member member) {
        if (member == null) return null;

        Long groupId = member.getGroup() != null ? member.getGroup().getId() : null;

        List<MarathonsDTO> marathons = Optional.ofNullable(member.getMarathons())
                .orElse(List.of())
                .stream()
                .map(m -> MarathonsDTO.builder()
                        .id(m.getId())
                        .identification(m.getIdentification())
                        .build())
                .toList();

        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .groupId(groupId)
                .marathons(marathons)
                .build();
    }

    @Test
    void testConvertListToEntity() {
        // Arrange
        MemberDTO dto1 = MemberDTO.builder()
                .name("User 1")
                .build();

        MemberDTO dto2 = MemberDTO.builder()
                .name("User 2")
                .build();

        List<MemberDTO> dtoList = List.of(dto1, dto2);

        // Act
        List<Member> members = memberConverter.convertToEntity(dtoList);

        // Assert
        assertNotNull(members);
        assertEquals(2, members.size());
        assertEquals("User 1", members.get(0).getName());
    }

    @Test
    void testConvertListToDto() {
        // Arrange
        Member member1 = Member.builder()
                .name("User A")
                .build();

        Member member2 = Member.builder()
                .name("User B")
                .build();

        List<Member> memberList = List.of(member1, member2);

        // Act
        List<MemberDTO> dtoList = memberConverter.convertToDto(memberList);

        // Assert
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("User A", dtoList.get(0).getName());
    }
}
