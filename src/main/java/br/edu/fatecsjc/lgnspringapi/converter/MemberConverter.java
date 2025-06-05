package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;

@Component
public class MemberConverter implements Converter<Member, MemberDTO> {

    private final ModelMapper modelMapper;
    private TypeMap<MemberDTO, Member> propertyMapperDto;

    @Autowired
    public MemberConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Member convertToEntity(MemberDTO dto) {
        if (dto == null) return null;

        ensureTypeMapConfigured();

        Provider<Member> memberProvider = p -> new Member();
        propertyMapperDto.setProvider(memberProvider);

        Member entity = modelMapper.map(dto, Member.class);

        handleMarathonBidirectionalRelationship(entity);

        return entity;
    }

    @Override
    public Member convertToEntity(MemberDTO dto, Member entity) {
        if (dto == null) return null;

        final Member targetEntity = (entity == null) ? new Member() : entity;

        ensureTypeMapConfigured();

        Provider<Member> memberProvider = p -> targetEntity;
        propertyMapperDto.setProvider(memberProvider);

        Member newEntity = modelMapper.map(dto, Member.class);

        handleMarathonBidirectionalRelationship(newEntity);

        return newEntity;
    }

    @Override
    public MemberDTO convertToDto(Member entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, MemberDTO.class);
    }

    @Override
    public List<Member> convertToEntity(List<MemberDTO> dtos) {
        if (dtos == null) return null;

        List<Member> members = modelMapper.map(dtos, new TypeToken<List<Member>>() {}.getType());

        members.forEach(this::handleMarathonBidirectionalRelationship);

        return members;
    }

    @Override
    public List<MemberDTO> convertToDto(List<Member> entities) {
        if (entities == null) return null;
        return modelMapper.map(entities, new TypeToken<List<MemberDTO>>() {}.getType());
    }

    private void ensureTypeMapConfigured() {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MemberDTO.class, Member.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Member::setId));
        }
    }

    private void handleMarathonBidirectionalRelationship(Member entity) {
        if (entity != null && entity.getMarathons() != null) {
            entity.getMarathons().forEach(marathon -> {
                if (marathon.getMembers() == null) {
                    marathon.setMembers(new ArrayList<>());
                }
                if (!marathon.getMembers().contains(entity)) {
                    marathon.getMembers().add(entity);
                }
            });
        }
    }
}
