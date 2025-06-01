package br.edu.fatecsjc.lgnspringapi.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class MemberTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        // Arrange
        Group group = new Group();
        group.setId(1L);
        group.setName("Runners Team");

        Marathons marathon1 = Marathons.builder()
                .id(1L)
                .identification("São Paulo Marathon")
                .build();

        Marathons marathon2 = Marathons.builder()
                .id(2L)
                .identification("Rio Marathon")
                .build();

        List<Marathons> marathons = new ArrayList<>();
        marathons.add(marathon1);
        marathons.add(marathon2);

        Member member = new Member(1L, "João Silva", "joao@example.com", group, marathons);

        // Assert
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("João Silva");
        assertThat(member.getEmail()).isEqualTo("joao@example.com");
        assertThat(member.getGroup()).isNotNull().isEqualTo(group);
        assertThat(member.getMarathons()).hasSize(2).containsExactlyInAnyOrderElementsOf(marathons);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        Member member = new Member();

        Group group = new Group();
        group.setId(2L);
        group.setName("Cyclist Group");

        Marathons marathon = Marathons.builder()
                .id(3L)
                .identification("Berlin Marathon")
                .build();

        List<Marathons> marathons = new ArrayList<>();
        marathons.add(marathon);

        // Act
        member.setId(2L);
        member.setName("Maria Oliveira");
        member.setEmail("maria@example.com");
        member.setGroup(group);
        member.setMarathons(marathons);

        // Assert
        assertThat(member.getId()).isEqualTo(2L);
        assertThat(member.getName()).isEqualTo("Maria Oliveira");
        assertThat(member.getEmail()).isEqualTo("maria@example.com");
        assertThat(member.getGroup()).isEqualTo(group);
        assertThat(member.getMarathons()).hasSize(1).containsExactly(marathon);
    }

    @Test
    void testBuilder() {
        // Arrange
        Group group = new Group();
        group.setId(3L);
        group.setName("Swim Team");

        Marathons marathon1 = Marathons.builder()
                .id(4L)
                .identification("Curitiba Marathon")
                .build();

        Marathons marathon2 = Marathons.builder()
                .id(5L)
                .identification("Brasília Marathon")
                .build();

        List<Marathons> marathons = new ArrayList<>();
        marathons.add(marathon1);
        marathons.add(marathon2);

        // Act
        Member member = Member.builder()
                .id(3L)
                .name("Carlos Souza")
                .email("carlos@example.com")
                .group(group)
                .marathons(marathons)
                .build();

        // Assert
        assertThat(member.getId()).isEqualTo(3L);
        assertThat(member.getName()).isEqualTo("Carlos Souza");
        assertThat(member.getEmail()).isEqualTo("carlos@example.com");
        assertThat(member.getGroup()).isEqualTo(group);
        assertThat(member.getMarathons()).hasSize(2).containsExactlyInAnyOrderElementsOf(marathons);
    }

    @Test
    void testDefaultMarathonsList() {
        // Act
        Member member = new Member();

        // Assert
        assertThat(member.getMarathons()).isNotNull().isEmpty();
    }

    @Test
    void testBidirectionalAssociation() {
        // Arrange
        Marathons marathon = Marathons.builder()
                .id(6L)
                .identification("Boston Marathon")
                .build();

        Member member = Member.builder()
                .id(4L)
                .name("Ana Costa")
                .email("ana@example.com")
                .marathons(new ArrayList<>())
                .build();

        // Act
        member.getMarathons().add(marathon);

        // Assert
        assertThat(member.getMarathons()).contains(marathon);
    }
}