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
    void testConvertToEntityWithExistingEntity() {
        // Arrange
        Member existingEntity = Member.builder()
                .id(999L)
                .name("Existing")
                .build();

        MarathonsDTO marathonDTO = MarathonsDTO.builder()
                .id(1L)
                .identification("Marathon Existing")
                .build();

        MemberDTO dto = MemberDTO.builder()
                .name("Updated Name")
                .groupId(300L)
                .marathons(List.of(marathonDTO))
                .build();

        // Act
        Member result = memberConverter.convertToEntity(dto, existingEntity);

        // Assert
        assertNotNull(result);
        assertEquals(existingEntity, result);
        assertEquals("Updated Name", result.getName());
        assertNotNull(result.getGroup());
        assertEquals(300L, result.getGroup().getId());

        assertNotNull(result.getMarathons());
        assertEquals(1, result.getMarathons().size());
        assertEquals("Marathon Existing", result.getMarathons().get(0).getIdentification());

        assertNotNull(result.getMarathons().get(0).getMembers());
        assertTrue(result.getMarathons().get(0).getMembers().contains(result));
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
    
    @Test
    void testConvertToDtoWithNull() {
        MemberDTO dto = memberConverter.convertToDto((Member) null);
        assertNull(dto);
    }

    @Test
    void testConvertToEntityWithNullList() {
        List<Member> result = memberConverter.convertToEntity((List<MemberDTO>) null);
        assertNull(result);
    }

    @Test
    void testConvertToDtoWithNullList() {
        List<MemberDTO> result = memberConverter.convertToDto((List<Member>) null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityWithEmptyMarathons() {
        MemberDTO dto = MemberDTO.builder()
                .name("No Marathon")
                .groupId(500L)
                .marathons(null)
                .build();

        Member result = memberConverter.convertToEntity(dto);

        assertNotNull(result);
        assertEquals("No Marathon", result.getName());
        assertNotNull(result.getGroup());
        assertEquals(500L, result.getGroup().getId());
        assertNull(result.getMarathons());
    }

    @Test
    void testPropertyMapperCacheBehavior() {
        MemberDTO dto = MemberDTO.builder()
                .name("First Call")
                .groupId(700L)
                .build();

        Member first = memberConverter.convertToEntity(dto);

        Member second = memberConverter.convertToEntity(dto);

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.getName(), second.getName());
    }
    
    @Test
    void shouldReturnNullWhenMappingNull() {
        MemberDTO dto = memberConverter.convertToDto((Member) null);
        assertNull(dto);
    }
}
