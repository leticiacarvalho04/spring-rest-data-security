package br.edu.fatecsjc.lgnspringapi.converter;

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
    @Autowired
    private ModelMapper modelMapper;

    private TypeMap<MemberDTO, Member> propertyMapperDto;

    @Override
    public Member convertToEntity(MemberDTO dto) {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MemberDTO.class, Member.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Member::setId));
        }

        Member entity = modelMapper.map(dto, Member.class);
        Provider<Member> memberProvider = p -> new Member();
        propertyMapperDto.setProvider(memberProvider);

        if (entity.getMarathons() != null) {
            entity.getMarathons().forEach(marathon -> {
                if (marathon.getMembers() == null) {
                    marathon.setMembers(new java.util.ArrayList<>());
                }
                marathon.getMembers().add(entity);
            });
        }

        return entity;
    }

    @Override
    public Member convertToEntity(MemberDTO dto, Member entity) {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MemberDTO.class, Member.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Member::setId));
        }

        Provider<Member> memberProvider = p -> entity;
        propertyMapperDto.setProvider(memberProvider);

        Member newEntity = modelMapper.map(dto, Member.class);

        if (newEntity.getMarathons() != null) {
            newEntity.getMarathons().forEach(marathon -> {
                if (marathon.getMembers() == null) {
                    marathon.setMembers(new java.util.ArrayList<>());
                }
                marathon.getMembers().add(newEntity);
            });
        }

        return newEntity;
    }

    @Override
    public MemberDTO convertToDto(Member entity) {
        return modelMapper.map(entity, MemberDTO.class);
    }

    @Override
    public List<Member> convertToEntity(List<MemberDTO> dtos) {
        List<Member> members = modelMapper.map(dtos, new TypeToken<List<Member>>() {}.getType());

        members.forEach(member -> {
            if (member.getMarathons() != null) {
                member.getMarathons().forEach(marathon -> {
                    if (marathon.getMembers() == null) {
                        marathon.setMembers(new java.util.ArrayList<>());
                    }
                    marathon.getMembers().add(member);
                });
            }
        });

        return members;
    }

    @Override
    public List<MemberDTO> convertToDto(List<Member> entities) {
        return modelMapper.map(entities, new TypeToken<List<MemberDTO>>() {}.getType());
    }
}
