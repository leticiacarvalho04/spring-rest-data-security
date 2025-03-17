package br.edu.fatecsjc.lgnspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.fatecsjc.lgnspringapi.converter.OrganizationConverter;
import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import jakarta.transaction.Transactional;

@Service
public class OrganizationService {
	@Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private OrganizationConverter organizationConverter;

    public List<OrganizationDTO> getAll() {
        return organizationConverter.convertToDto(organizationRepository.findAll());
    }

    public OrganizationDTO findById(Long id) {
        return organizationConverter.convertToDto(organizationRepository.findById(id).get());
    }

    @Transactional
    public OrganizationDTO save(Long id, OrganizationDTO dto) {
    	Organization entity = organizationRepository.findById(id).get();
        groupRepository.deleteGroupByOrganization(entity);
        entity.getGroup().clear();

        Organization organizationToSaved = organizationConverter.convertToEntity(dto, entity);
        organizationToSaved.getGroup().forEach( group -> {
            group.setOrganization(organizationToSaved);
        });
        Organization organizationReturned = organizationRepository.save(organizationToSaved);
        return organizationConverter.convertToDto(organizationReturned);
    }

    public OrganizationDTO save(OrganizationDTO dto) {
    	Organization organizationToSaved = organizationConverter.convertToEntity(dto);
    	Organization organizationReturned = organizationRepository.save(organizationToSaved);
        return organizationConverter.convertToDto(organizationReturned);
    }

    public void delete(Long id) {
    	organizationRepository.deleteById(id);
    }
}
