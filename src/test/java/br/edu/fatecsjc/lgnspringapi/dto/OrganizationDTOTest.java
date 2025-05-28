package br.edu.fatecsjc.lgnspringapi.dto;

import br.edu.fatecsjc.lgnspringapi.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationDTOTest {

    private OrganizationDTO organizationDTO;
    private Group group;

    @BeforeEach
    void setUp() {
        // Configura grupo
        group = new Group();
        group.setId(1L);
        group.setName("Developers");

        // Configura DTO
        organizationDTO = new OrganizationDTO();
        organizationDTO.setId(1L);
        organizationDTO.setName("Tech Corp");
        organizationDTO.setNumber("123");
        organizationDTO.setStreet("Main St");
        organizationDTO.setNeighborhood("Downtown");
        organizationDTO.setCEP("12345-678");
        organizationDTO.setMunicipality("São Paulo");
        organizationDTO.setState("SP");
        organizationDTO.setInstitutionName("Tech Institution");
        organizationDTO.setHostCountry("Brazil");
        organizationDTO.setGroup(List.of(group));
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertEquals(1L, organizationDTO.getId());
        assertEquals("Tech Corp", organizationDTO.getName());
        assertEquals("123", organizationDTO.getNumber());
        assertEquals("Main St", organizationDTO.getStreet());
        assertEquals("Downtown", organizationDTO.getNeighborhood());
        assertEquals("12345-678", organizationDTO.getCEP());
        assertEquals("São Paulo", organizationDTO.getMunicipality());
        assertEquals("SP", organizationDTO.getState());
        assertEquals("Tech Institution", organizationDTO.getInstitutionName());
        assertEquals("Brazil", organizationDTO.getHostCountry());
        assertNotNull(organizationDTO.getGroup());
        assertFalse(organizationDTO.getGroup().isEmpty());
        assertEquals("Developers", organizationDTO.getGroup().get(0).getName());
    }

    @Test
    void shouldUseNoArgsConstructor() {
        OrganizationDTO dto = new OrganizationDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getNumber());
        assertNull(dto.getStreet());
        assertNull(dto.getNeighborhood());
        assertNull(dto.getCEP());
        assertNull(dto.getMunicipality());
        assertNull(dto.getState());
        assertNull(dto.getInstitutionName());
        assertNull(dto.getHostCountry());
        assertNull(dto.getGroup());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        Group newGroup = new Group();
        newGroup.setId(2L);
        newGroup.setName("Designers");

        List<Group> groups = new ArrayList<>();
        groups.add(newGroup);

        OrganizationDTO dto = new OrganizationDTO(
                2L,
                "InovaTech",
                "456",
                "Second St",
                "Uptown",
                "87654-321",
                "Rio de Janeiro",
                "RJ",
                "Innovation School",
                "Brazil",
                groups
        );

        assertEquals(2L, dto.getId());
        assertEquals("InovaTech", dto.getName());
        assertEquals("456", dto.getNumber());
        assertEquals("Second St", dto.getStreet());
        assertEquals("Uptown", dto.getNeighborhood());
        assertEquals("87654-321", dto.getCEP());
        assertEquals("Rio de Janeiro", dto.getMunicipality());
        assertEquals("RJ", dto.getState());
        assertEquals("Innovation School", dto.getInstitutionName());
        assertEquals("Brazil", dto.getHostCountry());
        assertEquals("Designers", dto.getGroup().get(0).getName());
    }

    @Test
    void shouldUseBuilderCorrectly() {
        Group builtGroup = new Group();
        builtGroup.setId(3L);
        builtGroup.setName("Managers");

        OrganizationDTO dto = OrganizationDTO.builder()
                .id(3L)
                .name("Built Corp")
                .number("789")
                .street("Third St")
                .neighborhood("Midtown")
                .CEP("54321-098")
                .municipality("Curitiba")
                .state("PR")
                .institutionName("Built Institute")
                .hostCountry("Brazil")
                .group(List.of(builtGroup))
                .build();

        assertEquals(3L, dto.getId());
        assertEquals("Built Corp", dto.getName());
        assertEquals("789", dto.getNumber());
        assertEquals("Third St", dto.getStreet());
        assertEquals("Midtown", dto.getNeighborhood());
        assertEquals("54321-098", dto.getCEP());
        assertEquals("Curitiba", dto.getMunicipality());
        assertEquals("PR", dto.getState());
        assertEquals("Built Institute", dto.getInstitutionName());
        assertEquals("Brazil", dto.getHostCountry());
        assertEquals("Managers", dto.getGroup().get(0).getName());
    }

    @Test
    void shouldInitializeGroupListAsNullIfNotProvided() {
        OrganizationDTO dto = new OrganizationDTO();
        assertNull(dto.getGroup());
    }

    @Test
    void shouldGenerateToStringWithoutErrors() {
        String toString = organizationDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Tech Corp"));
        assertTrue(toString.contains("street=Main St"));
        assertTrue(toString.contains("neighborhood=Downtown"));
        assertTrue(toString.contains("CEP=12345-678"));
        assertTrue(toString.contains("municipality=São Paulo"));
        assertTrue(toString.contains("state=SP"));
        assertTrue(toString.contains("institutionName=Tech Institution"));
        assertTrue(toString.contains("hostCountry=Brazil"));
        assertTrue(toString.contains("group="));
    }

    @Test
    void shouldEqualsAndHashCodeWorkCorrectly() {
        Group groupA = new Group();
        groupA.setId(1L);
        groupA.setName("Team A");

        Group groupB = new Group();
        groupB.setId(2L);
        groupB.setName("Team B");

        OrganizationDTO dto1 = OrganizationDTO.builder()
                .id(1L)
                .name("Org A")
                .street("Street A")
                .neighborhood("Neigh A")
                .CEP("12345-678")
                .municipality("City A")
                .state("State A")
                .institutionName("Inst A")
                .hostCountry("Country A")
                .group(List.of(groupA))
                .build();

        OrganizationDTO dto2 = OrganizationDTO.builder()
                .id(1L)
                .name("Org A")
                .street("Street A")
                .neighborhood("Neigh A")
                .CEP("12345-678")
                .municipality("City A")
                .state("State A")
                .institutionName("Inst A")
                .hostCountry("Country A")
                .group(List.of(groupA))
                .build();

        OrganizationDTO differentDTO = OrganizationDTO.builder()
                .id(2L)
                .name("Org B")
                .street("Street B")
                .neighborhood("Neigh B")
                .CEP("87654-321")
                .municipality("City B")
                .state("State B")
                .institutionName("Inst B")
                .hostCountry("Country B")
                .group(List.of(groupB))
                .build();

        // Test equals
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, differentDTO);

        // Test hash code
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), differentDTO.hashCode());
    }
}