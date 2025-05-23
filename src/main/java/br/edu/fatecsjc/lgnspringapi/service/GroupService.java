package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupConverter groupConverter;

    public List<GroupDTO> getAll() {
        return groupConverter.convertToDto(groupRepository.findAll());
    }

    public GroupDTO findById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id " + id));
        return groupConverter.convertToDto(group);
    }

    @Transactional
    public GroupDTO save(Long id, GroupDTO dto) {
        Group entity = groupRepository.findById(id).orElse(null);
        if (entity != null) {
            memberRepository.deleteMembersByGroup(entity);
            entity.getMembers().clear();

            Group groupToSaved = groupConverter.convertToEntity(dto, entity);
            groupToSaved.getMembers().forEach(member -> {
                member.setGroup(groupToSaved);
            });

            Group groupReturned = groupRepository.save(groupToSaved);
            return groupConverter.convertToDto(groupReturned);
        }
        return null;
    }

    public GroupDTO save(GroupDTO dto) {
        Group groupToSaved = groupConverter.convertToEntity(dto);
        Group groupReturned = groupRepository.save(groupToSaved);
        return groupConverter.convertToDto(groupReturned);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
