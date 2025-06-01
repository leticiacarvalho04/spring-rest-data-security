package br.edu.fatecsjc.lgnspringapi.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        // Arrange
        Group group1 = Group.builder().id(1L).name("Runners").build();
        Group group2 = Group.builder().id(2L).name("Cyclists").build();

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);

        Organization organization = new Organization(
                1L,
                "FATEC Runners",
                "100",
                "Main St",
                "Downtown",
                "01000-000",
                "São Paulo",
                "SP",
                "FATEC SP",
                "Brazil",
                groups
        );

        // Assert
        assertThat(organization.getId()).isEqualTo(1L);
        assertThat(organization.getName()).isEqualTo("FATEC Runners");
        assertThat(organization.getNumber()).isEqualTo("100");
        assertThat(organization.getStreet()).isEqualTo("Main St");
        assertThat(organization.getNeighborhood()).isEqualTo("Downtown");
        assertThat(organization.getCep()).isEqualTo("01000-000");
        assertThat(organization.getMunicipality()).isEqualTo("São Paulo");
        assertThat(organization.getState()).isEqualTo("SP");
        assertThat(organization.getInstitutionName()).isEqualTo("FATEC SP");
        assertThat(organization.getHostCountry()).isEqualTo("Brazil");

        assertThat(organization.getGroups()).hasSize(2).containsExactlyInAnyOrder(group1, group2);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        Organization organization = new Organization();

        Group group = Group.builder().id(3L).name("Swimmers").build();

        List<Group> groups = new ArrayList<>();
        groups.add(group);

        // Act
        organization.setId(2L);
        organization.setName("FATEC Cyclists");
        organization.setNumber("200");
        organization.setStreet("Second Ave");
        organization.setNeighborhood("Uptown");
        organization.setCep("02000-000");
        organization.setMunicipality("Rio de Janeiro");
        organization.setState("RJ");
        organization.setInstitutionName("FATEC RJ");
        organization.setHostCountry("Brazil");
        organization.setGroups(groups);

        // Assert
        assertThat(organization.getId()).isEqualTo(2L);
        assertThat(organization.getName()).isEqualTo("FATEC Cyclists");
        assertThat(organization.getNumber()).isEqualTo("200");
        assertThat(organization.getStreet()).isEqualTo("Second Ave");
        assertThat(organization.getNeighborhood()).isEqualTo("Uptown");
        assertThat(organization.getCep()).isEqualTo("02000-000");
        assertThat(organization.getMunicipality()).isEqualTo("Rio de Janeiro");
        assertThat(organization.getState()).isEqualTo("RJ");
        assertThat(organization.getInstitutionName()).isEqualTo("FATEC RJ");
        assertThat(organization.getHostCountry()).isEqualTo("Brazil");

        assertThat(organization.getGroups()).hasSize(1).containsExactly(group);
    }

    @Test
    void testBuilder() {
        // Arrange
        Group group1 = Group.builder().id(4L).name("Hikers").build();
        Group group2 = Group.builder().id(5L).name("Triathletes").build();

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);

        // Act
        Organization organization = Organization.builder()
                .id(3L)
                .name("FATEC Hikers")
                .number("300")
                .street("Third St")
                .neighborhood("Midtown")
                .cep("03000-000")
                .municipality("Belo Horizonte")
                .state("MG")
                .institutionName("FATEC MG")
                .hostCountry("Brazil")
                .groups(groups)
                .build();

        // Assert
        assertThat(organization.getId()).isEqualTo(3L);
        assertThat(organization.getName()).isEqualTo("FATEC Hikers");
        assertThat(organization.getNumber()).isEqualTo("300");
        assertThat(organization.getStreet()).isEqualTo("Third St");
        assertThat(organization.getNeighborhood()).isEqualTo("Midtown");
        assertThat(organization.getCep()).isEqualTo("03000-000");
        assertThat(organization.getMunicipality()).isEqualTo("Belo Horizonte");
        assertThat(organization.getState()).isEqualTo("MG");
        assertThat(organization.getInstitutionName()).isEqualTo("FATEC MG");
        assertThat(organization.getHostCountry()).isEqualTo("Brazil");

        assertThat(organization.getGroups()).hasSize(2).containsExactlyInAnyOrder(group1, group2);
    }

    @Test
    void testDefaultGroupsList() {
        // Act
        Organization organization = new Organization();

        // Assert
        assertThat(organization.getGroups()).isNotNull().isEmpty();
    }

    @Test
    void testBidirectionalAssociation() {
        // Arrange
        Organization organization = Organization.builder()
                .id(4L)
                .name("FATEC Team")
                .build();

        Group group = Group.builder()
                .id(6L)
                .name("Marathoners")
                .organization(organization)
                .build();

        List<Group> groups = new ArrayList<>();
        groups.add(group);

        organization.setGroups(groups);

        // Assert
        assertThat(organization.getGroups()).contains(group);
        assertThat(group.getOrganization()).isEqualTo(organization);
    }
}