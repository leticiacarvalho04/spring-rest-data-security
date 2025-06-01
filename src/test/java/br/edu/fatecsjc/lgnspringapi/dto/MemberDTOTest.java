package br.edu.fatecsjc.lgnspringapi.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class MemberDTOTest {

    @Test
    void testBuilderAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "João Silva";
        Integer age = 25;
        Long groupId = 10L;

        List<MarathonsDTO> marathons = new ArrayList<>();
        marathons.add(MarathonsDTO.builder().identification("São Paulo Marathon").weight(2.5).build());
        marathons.add(MarathonsDTO.builder().identification("Rio Marathon").weight(3.5).build());

        // Act
        MemberDTO dto = MemberDTO.builder()
                .id(id)
                .name(name)
                .age(age)
                .groupId(groupId)
                .marathons(marathons)
                .build();

        // Assert
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAge()).isEqualTo(age);
        assertThat(dto.getGroupId()).isEqualTo(groupId);
        assertThat(dto.getMarathons()).isNotNull().hasSize(2).containsExactlyElementsOf(marathons);
    }

    @Test
    void testSetters() {
        // Arrange
        MemberDTO dto = new MemberDTO();
        Long id = 2L;
        String name = "Maria Souza";
        Integer age = 30;
        Long groupId = 5L;
        List<MarathonsDTO> marathons = new ArrayList<>();
        marathons.add(MarathonsDTO.builder().identification("Boston Marathon").weight(1.0).build());

        // Act
        dto.setId(id);
        dto.setName(name);
        dto.setAge(age);
        dto.setGroupId(groupId);
        dto.setMarathons(marathons);

        // Assert
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAge()).isEqualTo(age);
        assertThat(dto.getGroupId()).isEqualTo(groupId);
        assertThat(dto.getMarathons()).isNotNull().hasSize(1).containsExactlyElementsOf(marathons);
    }

    @Test
    void testDefaultMarathonsList() {
        // Act
        MemberDTO dto = new MemberDTO();

        // Assert
        assertThat(dto.getMarathons()).isNotNull().isEmpty();
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        MemberDTO dto = new MemberDTO();

        // Assert
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getAge()).isNull();
        assertThat(dto.getGroupId()).isNull();
        assertThat(dto.getMarathons()).isNotNull().isEmpty();
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String name = "Ana Oliveira";
        Integer age = 28;
        Long groupId = 7L;
        List<MarathonsDTO> marathons = new ArrayList<>();
        marathons.add(MarathonsDTO.builder().identification("Berlin Marathon").weight(1.5).build());

        // Act
        MemberDTO dto = new MemberDTO(id, name, age, groupId, marathons);

        // Assert
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAge()).isEqualTo(age);
        assertThat(dto.getGroupId()).isEqualTo(groupId);
        assertThat(dto.getMarathons()).isNotNull().hasSize(1).containsExactlyElementsOf(marathons);
    }
}