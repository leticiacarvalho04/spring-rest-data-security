package br.edu.fatecsjc.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;

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
}
