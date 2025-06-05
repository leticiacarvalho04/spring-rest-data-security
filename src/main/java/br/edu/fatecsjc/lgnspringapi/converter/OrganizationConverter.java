package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;

@Component
public class OrganizationConverter implements Converter<Organization, OrganizationDTO> {

    @Autowired
    private ModelMapper modelMapper;

    private TypeMap<OrganizationDTO, Organization> propertyMapperDto;

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Organization convertToEntity(OrganizationDTO dto) {
        if (dto == null) return null;

        ensureTypeMapConfigured();

        Provider<Organization> organizationProvider = p -> new Organization();
        propertyMapperDto.setProvider(organizationProvider);

        Organization entity = modelMapper.map(dto, Organization.class);

        handleBidirectionalRelationship(entity);

        return entity;
    }

    @Override
    public Organization convertToEntity(OrganizationDTO dto, Organization entity) {
        if (dto == null) return null;
        final Organization targetEntity = (entity == null) ? new Organization() : entity;

        ensureTypeMapConfigured();

        Provider<Organization> organizationProvider = p -> targetEntity;
        propertyMapperDto.setProvider(organizationProvider);

        Organization newEntity = modelMapper.map(dto, Organization.class);

        handleBidirectionalRelationship(newEntity);

        return newEntity;
    }

    @Override
    public OrganizationDTO convertToDto(Organization entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, OrganizationDTO.class);
    }

    @Override
    public List<Organization> convertToEntity(List<OrganizationDTO> dtos) {
        if (dtos == null) return null;

        List<Organization> organizations = modelMapper.map(dtos, new TypeToken<List<Organization>>() {}.getType());

        organizations.forEach(this::handleBidirectionalRelationship);

        return organizations;
    }

    @Override
    public List<OrganizationDTO> convertToDto(List<Organization> entities) {
        if (entities == null) return null;
        return modelMapper.map(entities, new TypeToken<List<OrganizationDTO>>() {}.getType());
    }

    private void ensureTypeMapConfigured() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(OrganizationDTO.class, Organization.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Organization::setId));
        }
    }

    private void handleBidirectionalRelationship(Organization organization) {
        if (organization != null && organization.getGroups() != null) {
            organization.getGroups().forEach(group -> group.setOrganization(organization));
        }
    }
}
