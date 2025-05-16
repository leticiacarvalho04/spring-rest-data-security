package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathons;

@Component
public class MarathonsConverter implements Converter<Marathons, MarathonsDTO> {
    @Autowired
    private ModelMapper modelMapper;

    private TypeMap<MarathonsDTO, Marathons> propertyMapperDto;

    @Override
    public Marathons convertToEntity(MarathonsDTO dto) {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MarathonsDTO.class, Marathons.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Marathons::setId));
        }

        Marathons entity = modelMapper.map(dto, Marathons.class);
        Provider<Marathons> marathonsProvider = p -> new Marathons();
        propertyMapperDto.setProvider(marathonsProvider);

        if (entity.getMembers() != null) {
            entity.getMembers().forEach(member -> {
                if (member.getMarathons() == null) {
                    member.setMarathons(new ArrayList<>());
                }
                member.getMarathons().add(entity);
            });
        }

        return entity;
    }

    @Override
    public Marathons convertToEntity(MarathonsDTO dto, Marathons entity) {
        if (propertyMapperDto == null) {
            propertyMapperDto = modelMapper.createTypeMap(MarathonsDTO.class, Marathons.class);
            propertyMapperDto.addMappings(mapper -> mapper.skip(Marathons::setId));
        }

        Provider<Marathons> marathonsProvider = p -> entity;
        propertyMapperDto.setProvider(marathonsProvider);
        Marathons newEntity = modelMapper.map(dto, Marathons.class);

        if (newEntity.getMembers() != null) {
            newEntity.getMembers().forEach(member -> {
                if (member.getMarathons() == null) {
                    member.setMarathons(new ArrayList<>());
                }
                member.getMarathons().add(newEntity);
            });
        }

        return newEntity;
    }

    @Override
    public MarathonsDTO convertToDto(Marathons entity) {
        return modelMapper.map(entity, MarathonsDTO.class);
    }

    @Override
    public List<Marathons> convertToEntity(List<MarathonsDTO> dtos) {
        List<Marathons> marathons = modelMapper.map(dtos, new TypeToken<List<Marathons>>(){}.getType());

        marathons.forEach(marathon -> {
            if (marathon.getMembers() != null) {
                marathon.getMembers().forEach(member -> {
                    if (member.getMarathons() == null) {
                        member.setMarathons(new ArrayList<>());
                    }
                    member.getMarathons().add(marathon);
                });
            }
        });

        return marathons;
    }

    @Override
    public List<MarathonsDTO> convertToDto(List<Marathons> entities) {
        return modelMapper.map(entities, new TypeToken<List<MarathonsDTO>>(){}.getType());
    }
}
