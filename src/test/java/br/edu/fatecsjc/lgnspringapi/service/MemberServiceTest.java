package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.MemberConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonsRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;
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
}