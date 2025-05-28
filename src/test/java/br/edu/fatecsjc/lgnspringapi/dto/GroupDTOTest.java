package br.edu.fatecsjc.lgnspringapi.dto;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class GroupDTOTest {

    private GroupDTO groupDTO;
    private MemberDTO memberDTO;
    private OrganizationDTO organizationDTO;

    @BeforeEach
    void setUp() {
        // Configura DTOs auxiliares
        memberDTO = MemberDTO.builder()
                .id(1L)
                .name("John Doe")
                .build();

        organizationDTO = OrganizationDTO.builder()
                .id(1L)
                .name("Tech Corp")
                .build();

        // Configura grupoDTO
        groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        groupDTO.setName("Developers");
        groupDTO.getMembers().add(memberDTO);
        groupDTO.setOrganization(organizationDTO);
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals(1L, groupDTO.getId());
        assertEquals("Developers", groupDTO.getName());
        assertNotNull(groupDTO.getMembers());
        assertFalse(groupDTO.getMembers().isEmpty());
        assertEquals("John Doe", groupDTO.getMembers().get(0).getName());
        assertNotNull(groupDTO.getOrganization());
        assertEquals("Tech Corp", groupDTO.getOrganization().getName());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        GroupDTO dto = new GroupDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNotNull(dto.getMembers());
        assertTrue(dto.getMembers().isEmpty());
        assertNull(dto.getOrganization());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        List<MemberDTO> members = new ArrayList<>();
        members.add(MemberDTO.builder().name("Jane Smith").build());

        OrganizationDTO org = OrganizationDTO.builder()
                .name("InovaTech")
                .build();

        GroupDTO dto = new GroupDTO(2L, "Designers", members, org);

        assertEquals(2L, dto.getId());
        assertEquals("Designers", dto.getName());
        assertEquals("Jane Smith", dto.getMembers().get(0).getName());
        assertEquals("InovaTech", dto.getOrganization().getName());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        MemberDTO member = MemberDTO.builder().name("Built Member").build();
        OrganizationDTO org = OrganizationDTO.builder().name("Built Org").build();

        GroupDTO dto = GroupDTO.builder()
                .id(3L)
                .name("Managers")
                .members(List.of(member))
                .organization(org)
                .build();

        assertEquals(3L, dto.getId());
        assertEquals("Managers", dto.getName());
        assertEquals("Built Member", dto.getMembers().get(0).getName());
        assertEquals("Built Org", dto.getOrganization().getName());
    }

    @Test
    void shouldInitializeMembersListAsArrayList() {
        assertNotNull(groupDTO.getMembers());
        assertTrue(groupDTO.getMembers() instanceof ArrayList);
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = groupDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Developers"));
        assertTrue(toString.contains("members="));
        assertTrue(toString.contains("organization=OrganizationDTO"));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        MemberDTO member = MemberDTO.builder().name("Member A").build();
        OrganizationDTO org = OrganizationDTO.builder().name("Org A").build();

        GroupDTO dto1 = GroupDTO.builder()
                .id(1L)
                .name("Team A")
                .members(List.of(member))
                .organization(org)
                .build();

        GroupDTO dto2 = GroupDTO.builder()
                .id(1L)
                .name("Team A")
                .members(List.of(member))
                .organization(org)
                .build();

        GroupDTO differentDTO = GroupDTO.builder()
                .id(2L)
                .name("Team B")
                .members(new ArrayList<>())
                .organization(OrganizationDTO.builder().name("Org B").build())
                .build();

        // Test equals
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, differentDTO);

        // Test hash code
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), differentDTO.hashCode());
    }
}