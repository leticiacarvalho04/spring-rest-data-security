package br.edu.fatecsjc.lgnspringapi.converter;

import br.edu.fatecsjc.lgnspringapi.converter.MarathonsConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathons;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class MarathonsConverterTest {

    private ModelMapper modelMapper;
    private MarathonsConverter marathonsConverter;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        marathonsConverter = new MarathonsConverter(modelMapper);
    }

    @Test
    void testConvertToEntity_NewEntity() {
        MemberDTO memberDTO = MemberDTO.builder().id(1L).name("John").build();
        MarathonsDTO dto = MarathonsDTO.builder()
                .id(1L)
                .identification("Marathon 1")
                .weight(70.5)
                .score(90)
                .members(Arrays.asList(memberDTO))
                .build();

        Marathons entity = marathonsConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertEquals("Marathon 1", entity.getIdentification());
        assertEquals(70.5, entity.getWeight());
        assertEquals(90, entity.getScore());
        assertNotNull(entity.getMembers());
        assertEquals(1, entity.getMembers().size());
        assertEquals("John", entity.getMembers().get(0).getName());
        assertTrue(entity.getMembers().get(0).getMarathons().contains(entity));
    }

    @Test
    void testConvertToEntity_ExistingEntity() {
        Marathons existing = new Marathons();
        existing.setId(1L);
        existing.setIdentification("Old Name");

        MarathonsDTO dto = MarathonsDTO.builder()
                .id(1L)
                .identification("Updated Name")
                .build();

        Marathons updated = marathonsConverter.convertToEntity(dto, existing);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getIdentification());
        assertEquals(1L, updated.getId().longValue());
    }

    @Test
    void testConvertToDto() {
        Member member = new Member();
        member.setId(1L);
        member.setName("Alice");

        Marathons entity = new Marathons();
        entity.setId(1L);
        entity.setIdentification("Ultra Marathon");
        entity.setWeight(80.0);
        entity.setScore(95);
        entity.setMembers(Arrays.asList(member));

        MarathonsDTO dto = marathonsConverter.convertToDto(entity);

        assertNotNull(dto);
        assertEquals("Ultra Marathon", dto.getIdentification());
        assertEquals(80.0, dto.getWeight());
        assertEquals(95, dto.getScore());
        assertNotNull(dto.getMembers());
        assertEquals(1, dto.getMembers().size());
        assertEquals("Alice", dto.getMembers().get(0).getName());
    }

    @Test
    void testConvertListToEntity() {
        MemberDTO memberDTO = MemberDTO.builder().id(1L).name("Bob").build();
        MarathonsDTO dto1 = MarathonsDTO.builder().id(1L).identification("A").members(Arrays.asList(memberDTO)).build();
        MarathonsDTO dto2 = MarathonsDTO.builder().id(2L).identification("B").build();

        List<Marathons> entities = marathonsConverter.convertToEntity(Arrays.asList(dto1, dto2));

        assertNotNull(entities);
        assertEquals(2, entities.size());

        Marathons m1 = entities.get(0);
        Marathons m2 = entities.get(1);

        assertEquals("A", m1.getIdentification());
        assertEquals("B", m2.getIdentification());
        assertNotNull(m1.getMembers());
        assertEquals("Bob", m1.getMembers().get(0).getName());
        assertTrue(m1.getMembers().get(0).getMarathons().contains(m1));
    }

    @Test
    void testConvertListToDto() {
        Member member = new Member();
        member.setId(1L);
        member.setName("Charlie");

        Marathons entity1 = new Marathons();
        entity1.setId(1L);
        entity1.setIdentification("X");
        entity1.setMembers(Arrays.asList(member));

        Marathons entity2 = new Marathons();
        entity2.setId(2L);
        entity2.setIdentification("Y");

        List<MarathonsDTO> dtos = marathonsConverter.convertToDto(Arrays.asList(entity1, entity2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("X", dtos.get(0).getIdentification());
        assertEquals("Y", dtos.get(1).getIdentification());
        assertNotNull(dtos.get(0).getMembers());
        assertEquals("Charlie", dtos.get(0).getMembers().get(0).getName());
    }
    
    @Test
    void shouldUpdateExistingMarathonAndReplaceMembers() {
        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setName("Alice");
        existingMember.setMarathons(new ArrayList<>());

        Marathons existing = new Marathons();
        existing.setId(1L);
        existing.setIdentification("Old Name");
        existing.setMembers(List.of(existingMember));

        MemberDTO newMemberDTO = MemberDTO.builder().id(2L).name("Bob").build();
        MarathonsDTO dto = MarathonsDTO.builder()
                .id(1L)
                .identification("Updated Marathon")
                .members(List.of(newMemberDTO))
                .build();

        Marathons updated = marathonsConverter.convertToEntity(dto, existing);

        assertNotNull(updated);
        assertEquals("Updated Marathon", updated.getIdentification());
        assertEquals(1, updated.getMembers().size());
        assertEquals("Bob", updated.getMembers().get(0).getName());

        // Verifica o relacionamento inverso
        assertTrue(updated.getMembers().get(0).getMarathons().contains(updated));
        assertFalse(existingMember.getMarathons().contains(updated)); // Antigo membro foi removido
    }
    
    @Test
    void testEnsureTypeMapConfigured_IsCached() throws Exception {
        Field propertyMapperDtoField = MarathonsConverter.class.getDeclaredField("propertyMapperDto");
        propertyMapperDtoField.setAccessible(true);

        // Primeira chamada
        marathonsConverter.convertToEntity(MarathonsDTO.builder().build());
        TypeMap<MarathonsDTO, Marathons> firstMap = (TypeMap<MarathonsDTO, Marathons>) propertyMapperDtoField.get(marathonsConverter);

        // Segunda chamada
        marathonsConverter.convertToEntity(MarathonsDTO.builder().build());
        TypeMap<MarathonsDTO, Marathons> secondMap = (TypeMap<MarathonsDTO, Marathons>) propertyMapperDtoField.get(marathonsConverter);

        assertSame(firstMap, secondMap);
    }
    
    @Test
    void shouldSetBidirectionalRelationshipForMultipleMembers() {
        MemberDTO m1 = MemberDTO.builder().id(1L).name("John").build();
        MemberDTO m2 = MemberDTO.builder().id(2L).name("Anna").build();

        MarathonsDTO dto = MarathonsDTO.builder()
                .identification("Multi Members")
                .members(Arrays.asList(m1, m2))
                .build();

        Marathons entity = marathonsConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertEquals(2, entity.getMembers().size());

        entity.getMembers().forEach(member -> {
            assertNotNull(member.getMarathons());
            assertTrue(member.getMarathons().contains(entity));
        });
    }
}
