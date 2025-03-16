package br.edu.fatecsjc.lgnspringapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.fatecsjc.lgnspringapi.converter.MarathonsConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathons;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonsRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.transaction.Transactional;

@Service
public class MarathonsService {
    @Autowired
    private MarathonsRepository marathonsRepository;   
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MarathonsConverter marathonsConverter;
    
    public void addMarathon(Member member, Marathons marathon) {
        if (member.getMarathons() == null) {
            member.setMarathons(new ArrayList<>());
        }
        member.getMarathons().add(marathon);
    }
    
    public List<MarathonsDTO> getAll(){
    	return marathonsConverter.convertToDto(marathonsRepository.findAll());
    }
    
    public MarathonsDTO findById(Long id) {
        return marathonsConverter.convertToDto(marathonsRepository.findById(id).get());
    }
    
    @Transactional
    public MarathonsDTO save(Long id, MarathonsDTO dto) {
        Marathons entity = marathonsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maratona não encontrada!"));

        memberRepository.deleteMembersByMarathons(entity);
        entity.getMembers().clear();

        Marathons marathonsToSaved = marathonsConverter.convertToEntity(dto, entity);

        if (marathonsToSaved.getMembers() != null) {
            marathonsToSaved.getMembers().forEach(member -> {
                if (member.getMarathons() == null) {
                    member.setMarathons(new ArrayList<>()); 
                }
                member.getMarathons().add(marathonsToSaved); 
            });
        }

        Marathons marathonReturned = marathonsRepository.save(marathonsToSaved);
        return marathonsConverter.convertToDto(marathonReturned);
    }
    
    public MarathonsDTO save(MarathonsDTO dto) {
    	Marathons marathonsToSaved = marathonsConverter.convertToEntity(dto);
    	Marathons marathonReturned = marathonsRepository.save(marathonsToSaved);
    	return marathonsConverter.convertToDto(marathonReturned);
    }
    
    public void delete(Long id) {
    	marathonsRepository.deleteById(id);
    }
}
