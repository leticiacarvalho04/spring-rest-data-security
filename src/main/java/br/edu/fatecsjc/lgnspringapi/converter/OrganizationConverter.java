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
public class OrganizationConverter  implements Converter<Organization, OrganizationDTO> {
	@Autowired
    private ModelMapper modelMapper;
	
	private TypeMap<OrganizationDTO, Organization> propertyMapperDto;
	
	@Override
    public Organization convertToEntity(OrganizationDTO dto) {
        if(propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(OrganizationDTO.class, Organization.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Organization::setId));
        }

        Organization entity = modelMapper.map(dto, Organization.class);
        Provider<Organization> organizationProvider = p -> new Organization();
        propertyMapperDto.setProvider(organizationProvider);

        entity.getGroup().forEach(m -> {
            m.setOrganization(entity);
        });
        return entity;
    }

    @Override
    public Organization convertToEntity(OrganizationDTO dto, Organization entity) {
        if(propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(OrganizationDTO.class, Organization.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Organization::setId));
        }

        Provider<Organization> organizationProvider = p -> entity;
        propertyMapperDto.setProvider(organizationProvider);

        Organization newEntity = modelMapper.map(dto, Organization.class);
        newEntity.getGroup().forEach(group -> {
            group.setOrganization(newEntity);
        });
        return newEntity;
    }

    @Override
    public OrganizationDTO convertToDto(Organization entity) {
        return modelMapper.map(entity, OrganizationDTO.class);
    }

    @Override
    public List<Organization> convertToEntity(List<OrganizationDTO> dtos) {
        List<Organization> organizations = modelMapper.map(dtos, new TypeToken<List<Organization>>(){}.getType());
        organizations.forEach(organization -> {
        	organization.getGroup().forEach(group -> {
                group.setOrganization(organization);
            });
        });
        return organizations;
    }

    @Override
    public List<OrganizationDTO> convertToDto(List<Organization> entities) {
        return modelMapper.map(entities, new TypeToken<List<OrganizationDTO>>(){}.getType());
    }
}
