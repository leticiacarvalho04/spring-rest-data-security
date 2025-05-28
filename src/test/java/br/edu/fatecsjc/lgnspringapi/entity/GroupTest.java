package br.edu.fatecsjc.lgnspringapi.entity;

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
class GroupTest {

    private Group group;
    private Organization organization;
    private Member member;

    @BeforeEach
    void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Tech Corp");

        member = new Member();
        member.setId(1L);
        member.setName("John Doe");

        group = new Group();
        group.setId(1L);
        group.setName("Developers");
        group.setOrganization(organization);
        group.getMembers().add(member);
    }

    @Test
    void shouldHaveValidInitialState() {
        assertNotNull(group);
        assertEquals(1L, group.getId());
        assertEquals("Developers", group.getName());
        assertNotNull(group.getOrganization());
        assertEquals("Tech Corp", group.getOrganization().getName());
    }

    @Test
    void shouldManageMembersListCorrectly() {
        List<Member> members = group.getMembers();

        assertNotNull(members);
        assertFalse(members.isEmpty());
        assertTrue(members.contains(member));
        assertEquals(1, members.size());
    }

    @Test
    void shouldSetFieldsCorrectlyUsingSetter() {
        Organization newOrg = new Organization();
        newOrg.setId(2L);
        newOrg.setName("InovaTech");

        group.setId(2L);
        group.setName("Designers");
        group.setOrganization(newOrg);

        assertEquals(2L, group.getId());
        assertEquals("Designers", group.getName());
        assertEquals("InovaTech", group.getOrganization().getName());
    }

    @Test
    void shouldInitializeMembersListAsArrayList() {
        assertTrue(group.getMembers() instanceof ArrayList);
    }

    @Test
    void shouldUseToStringWithoutErrors() {
        String toString = group.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Developers"));
        assertTrue(toString.contains("organization=Organization"));
    }
}