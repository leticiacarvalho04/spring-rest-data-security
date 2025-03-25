package br.edu.fatecsjc.lgnspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonsRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.transaction.Transactional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MarathonsRepository marathonRepository;
    
    @Autowired
    private MemberConverter memberConverter;

    public List<MemberDTO> getAll() {
        return memberConverter.convertToDto(memberRepository.findAll());
    }

    public MemberDTO findById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Membro não encontrado!"));
        return memberConverter.convertToDto(member);
    }

    @Transactional
    public MemberDTO save(Long id, MemberDTO dto) {
        Member entity = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Membro não encontrado!"));

        marathonRepository.deleteByMembers(entity);

        if (entity.getMarathons() != null) {
            entity.getMarathons().clear();
        }

        Member memberToSaved = memberConverter.convertToEntity(dto, entity);

        if (memberToSaved.getMarathons() != null) {
            memberToSaved.getMarathons().forEach(marathon -> {
                if (marathon.getMembers() == null) {
                    marathon.setMembers(new java.util.ArrayList<>()); 
                }
                marathon.getMembers().add(memberToSaved); 
            });
        }

        Member memberReturned = memberRepository.save(memberToSaved);
        return memberConverter.convertToDto(memberReturned);
    }

    public MemberDTO save(MemberDTO dto) {
        Member memberToSaved = memberConverter.convertToEntity(dto);
        Member memberReturned = memberRepository.save(memberToSaved);
        return memberConverter.convertToDto(memberReturned);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
