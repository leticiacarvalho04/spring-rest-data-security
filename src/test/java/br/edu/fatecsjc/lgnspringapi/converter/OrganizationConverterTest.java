package br.edu.fatecsjc.lgnspringapi.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.modelmapper.Provider;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class OrganizationConverterTest {

    private OrganizationConverter organizationConverter;

    @BeforeEach
    void setUp() {
        organizationConverter = new OrganizationConverter();
        organizationConverter.setModelMapper(new ModelMapper());
    }

    @Test
    void testConvertToEntity() {
        OrganizationDTO dto = OrganizationDTO.builder()
                .id(100L) 
                .name("Org Test")
                .number("123")
                .street("Main St")
                .neighborhood("Downtown")
                .CEP("12345-678")
                .municipality("City")
                .state("State")
                .institutionName("Institution")
                .hostCountry("Country")
                .group(new ArrayList<>())
                .build();

        Organization entity = organizationConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId(), "O ID deve ser null, pois foi ignorado no mapeamento.");
        assertEquals("Org Test", entity.getName());
        assertEquals("123", entity.getNumber());
        assertEquals("Main St", entity.getStreet());

        assertNotNull(entity.getGroups());
        assertEquals(0, entity.getGroups().size());
    }

    @Test
    void testConvertToDto() {
        Organization entity = Organization.builder()
                .id(10L)
                .name("Org Test")
                .number("123")
                .street("Main St")
                .neighborhood("Downtown")
                .cep("12345-678")
                .municipality("City")
                .state("State")
                .institutionName("Institution")
                .hostCountry("Country")
                .groups(null) 
                .build();

        OrganizationDTO dto = organizationConverter.convertToDto(entity);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Org Test", dto.getName());
        assertEquals("123", dto.getNumber());
        assertEquals("Main St", dto.getStreet());
    }

    @Test
    void testConvertListToEntity() {
        OrganizationDTO dto1 = OrganizationDTO.builder()
                .name("Org 1")
                .build();

        OrganizationDTO dto2 = OrganizationDTO.builder()
                .name("Org 2")
                .build();

        List<OrganizationDTO> dtoList = List.of(dto1, dto2);

        List<Organization> entityList = organizationConverter.convertToEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals("Org 1", entityList.get(0).getName());
        assertEquals("Org 2", entityList.get(1).getName());
    }

    @Test
    void testConvertListToDto() {
        Organization entity1 = Organization.builder()
                .name("Org A")
                .build();

        Organization entity2 = Organization.builder()
                .name("Org B")
                .build();

        List<Organization> entityList = List.of(entity1, entity2);

        List<OrganizationDTO> dtoList = organizationConverter.convertToDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Org A", dtoList.get(0).getName());
        assertEquals("Org B", dtoList.get(1).getName());
    }
    
    @Test
    void testConvertToEntityWithNullGroups() {
        OrganizationDTO dto = OrganizationDTO.builder()
                .name("Org Test")
                .group(null)
                .build();

        Organization entity = organizationConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getGroups());
        assertTrue(entity.getGroups().isEmpty(), "Deveria ser lista vazia");
    }

    @Test
    void testConvertToEntityWithEmptyGroupsList() {
        OrganizationDTO dto = OrganizationDTO.builder()
                .name("Org Empty Groups")
                .group(List.of())
                .build();

        Organization entity = organizationConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getGroups());
        assertTrue(entity.getGroups().isEmpty());
    }

    @Test
    void testConvertEmptyListToEntity() {
        List<Organization> result = organizationConverter.convertToEntity(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertEmptyListToDto() {
        List<OrganizationDTO> result = organizationConverter.convertToDto(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertNullListToEntity() {
        List<Organization> result = organizationConverter.convertToEntity((List<OrganizationDTO>) null);
        assertNull(result);
    }

    @Test
    void testConvertNullListToDto() {
        List<OrganizationDTO> result = organizationConverter.convertToDto((List<Organization>) null);
        assertNull(result);
    }

    @Test
    void testPropertyMapperCacheBehavior() {
        OrganizationDTO dto1 = OrganizationDTO.builder().name("Org One").build();
        OrganizationDTO dto2 = OrganizationDTO.builder().name("Org Two").build();

        Organization first = organizationConverter.convertToEntity(dto1);
        Organization second = organizationConverter.convertToEntity(dto2);

        assertNotNull(first);
        assertNotNull(second);
        assertEquals("Org One", first.getName());
        assertEquals("Org Two", second.getName());
    }

    @Test
    void testConvertToEntityWithNullDTO() {
        Organization result = organizationConverter.convertToEntity((OrganizationDTO) null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityWithExistingEntityAsNull() {
        OrganizationDTO dto = OrganizationDTO.builder()
                .name("Org Test")
                .group(List.of())
                .build();

        Organization result = organizationConverter.convertToEntity(dto, null);

        assertNotNull(result);
        assertEquals("Org Test", result.getName());
        assertNotNull(result.getGroups());
        assertTrue(result.getGroups().isEmpty());
    }

    @Test
    void testConvertToEntityWithNullList() {
        List<Organization> result = organizationConverter.convertToEntity((List<OrganizationDTO>) null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityWithEmptyList() {
        List<Organization> result = organizationConverter.convertToEntity(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertToDtoWithNullList() {
        List<OrganizationDTO> result = organizationConverter.convertToDto((List<Organization>) null);
        assertNull(result);
    }

    @Test
    void testConvertToDtoWithEmptyList() {
        List<OrganizationDTO> result = organizationConverter.convertToDto(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertToEntityWithGroupNull() {
        OrganizationDTO dto = OrganizationDTO.builder()
                .name("Org No Group")
                .group(null)
                .build();

        Organization entity = organizationConverter.convertToEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getGroups()); 
        assertTrue(entity.getGroups().isEmpty());
    }

    @Test
    void testConvertToEntity_WithExistingEntity_ShouldReuseAndPopulate() {
        Organization existingEntity = Organization.builder()
                .id(50L)
                .name("Old Name")
                .build();

        OrganizationDTO dto = OrganizationDTO.builder()
                .name("Updated Name")
                .build();

        Organization updatedEntity = organizationConverter.convertToEntity(dto, existingEntity);

        assertNotNull(updatedEntity);
        assertEquals(50L, updatedEntity.getId()); 
        assertEquals("Updated Name", updatedEntity.getName()); 
    }
}
