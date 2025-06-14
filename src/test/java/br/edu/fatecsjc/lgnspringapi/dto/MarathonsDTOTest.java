package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MarathonsDTOTest {

    private MarathonsDTO marathonDTO;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        memberDTO = MemberDTO.builder()
                .id(1L)
                .name("John Doe")
                .build();

        marathonDTO = new MarathonsDTO();
        marathonDTO.setId(1L);
        marathonDTO.setIdentification("Spring Run");
        marathonDTO.setWeight(75.5);
        marathonDTO.setScore(90);
        marathonDTO.setMembers(List.of(memberDTO));
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals(1L, marathonDTO.getId());
        assertEquals("Spring Run", marathonDTO.getIdentification());
        assertEquals(75.5, marathonDTO.getWeight());
        assertEquals(90, marathonDTO.getScore());
        assertNotNull(marathonDTO.getMembers());
        assertFalse(marathonDTO.getMembers().isEmpty());
        assertEquals("John Doe", marathonDTO.getMembers().get(0).getName());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        MarathonsDTO dto = new MarathonsDTO();
        assertThat(dto).isNotNull();
    }

    @Test
    void shouldUseAllArgsConstructor() {
        List<MemberDTO> members = List.of(MemberDTO.builder()
                .name("Jane Smith")
                .build());

        MarathonsDTO dto = new MarathonsDTO(2L, "Winter Run", 80.0, 95, members);

        assertEquals(2L, dto.getId());
        assertEquals("Winter Run", dto.getIdentification());
        assertEquals(80.0, dto.getWeight());
        assertEquals(95, dto.getScore());
        assertEquals("Jane Smith", dto.getMembers().get(0).getName());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        MemberDTO member = MemberDTO.builder()
                .id(2L)
                .name("Built Member")
                .build();

        MarathonsDTO dto = MarathonsDTO.builder()
                .id(3L)
                .identification("Summer Marathon")
                .weight(85.0)
                .score(92)
                .members(List.of(member))
                .build();

        assertEquals(3L, dto.getId());
        assertEquals("Summer Marathon", dto.getIdentification());
        assertEquals(85.0, dto.getWeight());
        assertEquals(92, dto.getScore());
        assertEquals("Built Member", dto.getMembers().get(0).getName());
    }

    @Test
    void shouldInitializeMembersListAsNullIfNotProvided() {
        MarathonsDTO dto = new MarathonsDTO();
        assertNull(dto.getMembers());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = marathonDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("identification=Spring Run"));
        assertTrue(toString.contains("weight=75.5"));
        assertTrue(toString.contains("score=90"));
        assertTrue(toString.contains("members="));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        MemberDTO memberA = MemberDTO.builder().name("Member A").build();
        MemberDTO memberB = MemberDTO.builder().name("Member B").build();

        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run A")
                .weight(70.0)
                .score(85)
                .members(List.of(memberA))
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run A")
                .weight(70.0)
                .score(85)
                .members(List.of(memberA))
                .build();

        MarathonsDTO differentDTO = MarathonsDTO.builder()
                .id(2L)
                .identification("Run B")
                .weight(75.0)
                .score(90)
                .members(List.of(memberB))
                .build();

        // Test equals
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, differentDTO);

        // Test hash code
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), differentDTO.hashCode());
    }

    @Test
    void shouldEqualsHandleNullAndOtherTypes() {
        MarathonsDTO dto = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a MarathonsDTO");
        assertEquals(dto, dto); // reflexivo
    }

    @Test
    void shouldHandleNullFieldsInEqualsAndHashCodeAndToString() {
        MarathonsDTO dto1 = new MarathonsDTO();
        MarathonsDTO dto2 = new MarathonsDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("MarathonsDTO"));
    }

    @Test
    void shouldNotBeEqualWhenOnlyIdDiffers() {
        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(2L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyIdentificationDiffers() {
        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("RunA")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(1L)
                .identification("RunB")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyWeightDiffers() {
        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(20.0)
                .score(1)
                .members(List.of())
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyScoreDiffers() {
        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of())
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(2)
                .members(List.of())
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqualWhenOnlyMembersDiffers() {
        MemberDTO memberA = MemberDTO.builder().name("A").build();
        MemberDTO memberB = MemberDTO.builder().name("B").build();

        MarathonsDTO dto1 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of(memberA))
                .build();

        MarathonsDTO dto2 = MarathonsDTO.builder()
                .id(1L)
                .identification("Run")
                .weight(10.0)
                .score(1)
                .members(List.of(memberB))
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldUseAllArgsConstructorWithNulls() {
        MarathonsDTO dto = new MarathonsDTO(null, null, null, 0, null);
        assertNull(dto.getId());
        assertNull(dto.getIdentification());
        assertNull(dto.getWeight());
        assertEquals(0, dto.getScore());
        assertNull(dto.getMembers());
    }

    @Test
    void shouldUseBuilderWithNulls() {
        MarathonsDTO dto = MarathonsDTO.builder()
                .id(null)
                .identification(null)
                .weight(null)
                .score(0)
                .members(null)
                .build();
        assertNull(dto.getId());
        assertNull(dto.getIdentification());
        assertNull(dto.getWeight());
        assertEquals(0, dto.getScore());
        assertNull(dto.getMembers());
    }
}