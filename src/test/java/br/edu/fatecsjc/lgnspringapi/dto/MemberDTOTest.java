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

    @Test
    void testNoArgsConstructor_InitializesFieldsCorrectly() {
        MemberDTO dto = new MemberDTO();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getAge()).isNull();
        assertThat(dto.getGroupId()).isNull();
        assertThat(dto.getMarathons()).isNotNull().isEmpty();
    }

    @Test
    void testGettersAfterBuildingWithAllFields() {
        MarathonsDTO marathon1 = MarathonsDTO.builder().identification("São Paulo Marathon").build();
        MarathonsDTO marathon2 = MarathonsDTO.builder().identification("Rio Marathon").build();

        MemberDTO dto = MemberDTO.builder()
                .id(1L)
                .name("João Silva")
                .age(30)
                .groupId(5L)
                .marathons(List.of(marathon1, marathon2))
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("João Silva");
        assertThat(dto.getAge()).isEqualTo(30);
        assertThat(dto.getGroupId()).isEqualTo(5L);
        assertThat(dto.getMarathons()).containsExactly(marathon1, marathon2);
    }

    @Test
    void testSetters_ModifyFieldValues() {
        MemberDTO dto = new MemberDTO();
        MarathonsDTO marathon = MarathonsDTO.builder().identification("Berlin Marathon").build();

        dto.setId(2L);
        dto.setName("Maria Oliveira");
        dto.setAge(25);
        dto.setGroupId(3L);
        dto.setMarathons(List.of(marathon));

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Maria Oliveira");
        assertThat(dto.getAge()).isEqualTo(25);
        assertThat(dto.getGroupId()).isEqualTo(3L);
        assertThat(dto.getMarathons()).containsExactly(marathon);
    }

    @Test
    void testAllArgsConstructor_InitializesAllFields() {
        MarathonsDTO marathon = MarathonsDTO.builder().identification("Boston Marathon").build();
        List<MarathonsDTO> marathons = List.of(marathon);

        MemberDTO dto = new MemberDTO(1L, "Carlos", 40, 2L, marathons);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Carlos");
        assertThat(dto.getAge()).isEqualTo(40);
        assertThat(dto.getGroupId()).isEqualTo(2L);
        assertThat(dto.getMarathons()).containsExactly(marathon);
    }

    @Test
    void testDefaultMarathonsList_IsEmptyAndNotNull() {
        MemberDTO dto = MemberDTO.builder().build();
        assertThat(dto.getMarathons()).isNotNull().isEmpty();
    }

    @Test
    void testAddAndRemoveMarathonsFromList() {
        MemberDTO dto = MemberDTO.builder().build();

        MarathonsDTO m1 = MarathonsDTO.builder().identification("NYC Marathon").build();
        MarathonsDTO m2 = MarathonsDTO.builder().identification("Tokyo Marathon").build();

        dto.getMarathons().add(m1);
        dto.getMarathons().add(m2);
        dto.getMarathons().remove(m1);

        assertThat(dto.getMarathons()).containsOnly(m2);
    }

    @Test
    void testEqualsAndHashCode() {
        MarathonsDTO m1 = MarathonsDTO.builder().identification("World Marathon").build();
        MarathonsDTO m2 = MarathonsDTO.builder().identification("World Marathon").build();

        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").marathons(List.of(m1)).build();
        MemberDTO dto2 = MemberDTO.builder().id(1L).name("Alice").marathons(List.of(m2)).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testEqualsWithNullAndOtherType() {
        MemberDTO dto = MemberDTO.builder().id(1L).name("Alice").build();
        assertThat(dto).isNotEqualTo(null);
        assertThat(dto).isNotEqualTo("not a MemberDTO");
        assertThat(dto).isEqualTo(dto); // reflexivo
    }

    @Test
    void testEqualsAndHashCodeWithNullFields() {
        MemberDTO dto1 = new MemberDTO();
        MemberDTO dto2 = new MemberDTO();
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1.toString()).contains("MemberDTO");
    }

    @Test
    void testNotEqualsWhenOnlyIdDiffers() {
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").build();
        MemberDTO dto2 = MemberDTO.builder().id(2L).name("Alice").build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testNotEqualsWhenOnlyNameDiffers() {
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").build();
        MemberDTO dto2 = MemberDTO.builder().id(1L).name("Bob").build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testNotEqualsWhenOnlyAgeDiffers() {
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").age(20).build();
        MemberDTO dto2 = MemberDTO.builder().id(1L).name("Alice").age(21).build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testNotEqualsWhenOnlyGroupIdDiffers() {
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").groupId(1L).build();
        MemberDTO dto2 = MemberDTO.builder().id(1L).name("Alice").groupId(2L).build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testNotEqualsWhenOnlyMarathonsDiffers() {
        MarathonsDTO m1 = MarathonsDTO.builder().identification("A").build();
        MarathonsDTO m2 = MarathonsDTO.builder().identification("B").build();
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("Alice").marathons(List.of(m1)).build();
        MemberDTO dto2 = MemberDTO.builder().id(1L).name("Alice").marathons(List.of(m2)).build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testAllArgsConstructorWithNulls() {
        MemberDTO dto = new MemberDTO(null, null, null, null, null);
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getAge()).isNull();
        assertThat(dto.getGroupId()).isNull();
        assertThat(dto.getMarathons()).isNull();
    }

    @Test
    void testBuilderWithNulls() {
        MemberDTO dto = MemberDTO.builder()
                .id(null)
                .name(null)
                .age(null)
                .groupId(null)
                .marathons(null)
                .build();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getAge()).isNull();
        assertThat(dto.getGroupId()).isNull();
        assertThat(dto.getMarathons()).isNull();
    }

    @Test
    void testToStringWithNullFields() {
        MemberDTO dto = new MemberDTO(null, null, null, null, null);
        String str = dto.toString();
        assertThat(str).contains("MemberDTO");
    }

    @Test
    void testEqualsAndHashCode_ListNullVsEmpty() {
        MemberDTO dtoNullList = MemberDTO.builder().id(1L).name("Alice").marathons(null).build();
        MemberDTO dtoEmptyList = MemberDTO.builder().id(1L).name("Alice").marathons(new ArrayList<>()).build();
        assertThat(dtoNullList).isNotEqualTo(dtoEmptyList);
        assertThat(dtoNullList.hashCode()).isNotEqualTo(dtoEmptyList.hashCode());
    }

    @Test
    void testEqualsAndHashCode_Consistent() {
        MemberDTO dto = MemberDTO.builder().id(1L).name("Alice").build();
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void testToStringWithFieldsFilled() {
        MarathonsDTO marathon = MarathonsDTO.builder().identification("Filled").build();
        MemberDTO dto = MemberDTO.builder()
                .id(99L)
                .name("Filled Name")
                .age(50)
                .groupId(77L)
                .marathons(List.of(marathon))
                .build();
        String str = dto.toString();
        assertThat(str).contains("MemberDTO");
        assertThat(str).contains("Filled Name");
        assertThat(str).contains("99");
        assertThat(str).contains("77");
        assertThat(str).contains("50");
        assertThat(str).contains("Filled");
    }

    @Test
    void testEqualsWithAllFieldsDifferent() {
        MemberDTO dto1 = MemberDTO.builder().id(1L).name("A").age(10).groupId(1L).marathons(List.of()).build();
        MemberDTO dto2 = MemberDTO.builder().id(2L).name("B").age(20).groupId(2L)
                .marathons(List.of(MarathonsDTO.builder().identification("X").build())).build();
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testEqualsWithSameReference() {
        MemberDTO dto = MemberDTO.builder().id(1L).build();
        assertThat(dto).isEqualTo(dto);
    }
}