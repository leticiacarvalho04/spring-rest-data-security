package br.edu.fatecsjc.lgnspringapi.repository;

import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void shouldDeleteGroupsByOrganization_whenValidOrganizationIsProvided() {
        // Arrange
        Organization org = new Organization();
        org.setName("Tech Corp");
        organizationRepository.save(org);

        Group group = new Group();
        group.setName("Grupo 1");
        group.setOrganization(org);
        groupRepository.save(group);

        // Act
        groupRepository.deleteGroupByOrganization(org);

        // Assert
        assertThat(groupRepository.findAll()).isEmpty();
    }
}