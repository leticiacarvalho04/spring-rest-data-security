package br.edu.fatecsjc.lgnspringapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MarathonsTest {

    private Marathons marathon;
    private Member member;

    @BeforeEach
    void setUp() {
        marathon = new Marathons();
        marathon.setId(1L);
        marathon.setIdentification("Spring Run");
        marathon.setWeight(75.5);
        marathon.setScore(90);

        member = new Member();
        member.setId(1L);
        member.setName("John Doe");

        // Inicializa lista de membros
        List<Member> members = new ArrayList<>();
        members.add(member);
        marathon.setMembers(members);
    }

    @Test
    void shouldHaveValidInitialState() {
        assertNotNull(marathon);
        assertEquals(1L, marathon.getId());
        assertEquals("Spring Run", marathon.getIdentification());
        assertEquals(75.5, marathon.getWeight());
        assertEquals(90, marathon.getScore());
        assertNotNull(marathon.getMembers());
        assertFalse(marathon.getMembers().isEmpty());
        assertEquals("John Doe", marathon.getMembers().get(0).getName());
    }

    @Test
    void shouldSetFieldsCorrectlyUsingSetter() {
        marathon.setId(2L);
        marathon.setIdentification("Winter Marathon");
        marathon.setWeight(80.0);
        marathon.setScore(95);

        assertEquals(2L, marathon.getId());
        assertEquals("Winter Marathon", marathon.getIdentification());
        assertEquals(80.0, marathon.getWeight());
        assertEquals(95, marathon.getScore());
    }

    @Test
    void shouldManageMembersListCorrectly() {
        List<Member> members = marathon.getMembers();

        assertNotNull(members);
        assertEquals(1, members.size());
        assertTrue(members.contains(member));
    }

    @Test
    void shouldUseToStringWithoutErrors() {
        String toString = marathon.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("identification=Spring Run"));
        assertTrue(toString.contains("weight=75.5"));
        assertTrue(toString.contains("score=90"));
    }
}