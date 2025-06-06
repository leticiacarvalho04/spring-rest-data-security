package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathons;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonsRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MarathonsRepository marathonRepository;
    @Mock
    private MemberConverter memberConverter;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllMembers() {
        when(memberRepository.findAll()).thenReturn(Collections.singletonList(new Member()));
        when(memberConverter.convertToDto(anyList())).thenReturn(Collections.singletonList(new MemberDTO()));
        assertEquals(1, memberService.getAll().size());
    }

    @Test
    void shouldReturnMemberById() {
        Member member = new Member();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberConverter.convertToDto(member)).thenReturn(new MemberDTO());
        assertNotNull(memberService.findById(1L));
    }

    @Test
    void shouldReturnNullWhenMemberNotFoundById() {
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(memberService.findById(2L));
    }

    @Test
    void shouldSaveMember() {
        MemberDTO dto = new MemberDTO();
        Member member = new Member();
        when(memberConverter.convertToEntity(dto)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberConverter.convertToDto(member)).thenReturn(dto);
        assertNotNull(memberService.save(dto));
    }

    @Test
    void shouldDeleteMember() {
        memberService.delete(1L);
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldSaveMemberWithIdWhenExistsAndWithMarathons() {
        Long id = 1L;
        MemberDTO dto = new MemberDTO();
        Member existing = new Member();
        Marathons marathon = new Marathons();
        List<Marathons> marathonsList = new ArrayList<>();
        marathonsList.add(marathon);
        existing.setMarathons(marathonsList);

        Member memberToSaved = new Member();
        memberToSaved.setMarathons(marathonsList);

        Member memberReturned = new Member();
        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(marathonRepository).deleteByMembers(existing);
        when(memberConverter.convertToEntity(dto, existing)).thenReturn(memberToSaved);
        when(memberRepository.save(memberToSaved)).thenReturn(memberReturned);
        when(memberConverter.convertToDto(memberReturned)).thenReturn(dto);

        MemberDTO result = memberService.save(id, dto);
        assertNotNull(result);
        verify(marathonRepository).deleteByMembers(existing);
        verify(memberRepository).save(memberToSaved);
    }

    @Test
    void shouldSaveMemberWithIdWhenExistsAndWithNullMarathons() {
        Long id = 2L;
        MemberDTO dto = new MemberDTO();
        Member existing = new Member();
        existing.setMarathons(null);

        Member memberToSaved = new Member();
        memberToSaved.setMarathons(null);

        Member memberReturned = new Member();
        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(marathonRepository).deleteByMembers(existing);
        when(memberConverter.convertToEntity(dto, existing)).thenReturn(memberToSaved);
        when(memberRepository.save(memberToSaved)).thenReturn(memberReturned);
        when(memberConverter.convertToDto(memberReturned)).thenReturn(dto);

        MemberDTO result = memberService.save(id, dto);
        assertNotNull(result);
        verify(marathonRepository).deleteByMembers(existing);
        verify(memberRepository).save(memberToSaved);
    }

    @Test
    void shouldReturnNullWhenSaveMemberWithIdNotFound() {
        Long id = 3L;
        MemberDTO dto = new MemberDTO();
        when(memberRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(memberService.save(id, dto));
    }

    @Test
    void shouldSaveMemberWithIdWhenExistsAndWithEmptyMarathons() {
        Long id = 4L;
        MemberDTO dto = new MemberDTO();
        Member existing = new Member();
        existing.setMarathons(new ArrayList<>()); // lista vazia

        Member memberToSaved = new Member();
        memberToSaved.setMarathons(new ArrayList<>());

        Member memberReturned = new Member();
        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(marathonRepository).deleteByMembers(existing);
        when(memberConverter.convertToEntity(dto, existing)).thenReturn(memberToSaved);
        when(memberRepository.save(memberToSaved)).thenReturn(memberReturned);
        when(memberConverter.convertToDto(memberReturned)).thenReturn(dto);

        MemberDTO result = memberService.save(id, dto);
        assertNotNull(result);
        verify(marathonRepository).deleteByMembers(existing);
        verify(memberRepository).save(memberToSaved);
    }

    @Test
    void shouldSaveMemberWithIdWhenExistsAndWithMarathonsListWithNullElement() {
        Long id = 5L;
        MemberDTO dto = new MemberDTO();
        Member existing = new Member();
        List<Marathons> marathonsList = new ArrayList<>();
        marathonsList.add(null); // lista com elemento nulo
        existing.setMarathons(marathonsList);

        Member memberToSaved = new Member();
        memberToSaved.setMarathons(marathonsList);

        Member memberReturned = new Member();
        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(marathonRepository).deleteByMembers(existing);
        when(memberConverter.convertToEntity(dto, existing)).thenReturn(memberToSaved);
        when(memberRepository.save(memberToSaved)).thenReturn(memberReturned);
        when(memberConverter.convertToDto(memberReturned)).thenReturn(dto);

        MemberDTO result = memberService.save(id, dto);
        assertNotNull(result);
        verify(marathonRepository).deleteByMembers(existing);
        verify(memberRepository).save(memberToSaved);
    }
}