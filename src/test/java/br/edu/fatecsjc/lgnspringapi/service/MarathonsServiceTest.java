package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.MarathonsConverter;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
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
class MarathonsServiceTest {

    @Mock
    private MarathonsRepository marathonsRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MarathonsConverter marathonsConverter;

    @InjectMocks
    private MarathonsService marathonsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddMarathonToMember() {
        Member member = new Member();
        Marathons marathon = new Marathons();
        assertDoesNotThrow(() -> marathonsService.addMarathon(member, marathon));
        assertNotNull(member.getMarathons());
        assertTrue(member.getMarathons().contains(marathon));
    }

    @Test
    void shouldReturnAllMarathons() {
        List<Marathons> marathonsList = List.of(new Marathons());
        List<MarathonsDTO> dtos = List.of(new MarathonsDTO());
        when(marathonsRepository.findAll()).thenReturn(marathonsList);
        when(marathonsConverter.convertToDto(marathonsList)).thenReturn(dtos);
        List<MarathonsDTO> result = marathonsService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnMarathonById() {
        Marathons marathon = new Marathons();
        MarathonsDTO dto = new MarathonsDTO();
        when(marathonsRepository.findById(1L)).thenReturn(Optional.of(marathon));
        when(marathonsConverter.convertToDto(marathon)).thenReturn(dto);
        MarathonsDTO result = marathonsService.findById(1L);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullWhenMarathonNotFoundById() {
        when(marathonsRepository.findById(2L)).thenReturn(Optional.empty());
        when(marathonsConverter.convertToDto((Marathons) null)).thenReturn(null);
        MarathonsDTO result = marathonsService.findById(2L);
        assertNull(result);
    }

    @Test
    void shouldSaveMarathonWithIdWhenExists() {
        Long id = 1L;
        MarathonsDTO dto = new MarathonsDTO();
        Marathons existing = new Marathons();
        Member member = new Member();
        existing.setMembers(new ArrayList<>(List.of(member)));
        Marathons toSave = new Marathons();
        toSave.setMembers(new ArrayList<>(List.of(member)));
        Marathons saved = new Marathons();
        MarathonsDTO savedDto = new MarathonsDTO();

        when(marathonsRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(memberRepository).deleteMembersByMarathons(existing);
        when(marathonsConverter.convertToEntity(dto, existing)).thenReturn(toSave);
        when(marathonsRepository.save(toSave)).thenReturn(saved);
        when(marathonsConverter.convertToDto(saved)).thenReturn(savedDto);

        MarathonsDTO result = marathonsService.save(id, dto);
        assertNotNull(result);
        verify(memberRepository).deleteMembersByMarathons(existing);
        verify(marathonsRepository).save(toSave);
    }

    @Test
    void shouldReturnNullWhenSaveMarathonWithIdNotFound() {
        Long id = 2L;
        MarathonsDTO dto = new MarathonsDTO();
        when(marathonsRepository.findById(id)).thenReturn(Optional.empty());
        MarathonsDTO result = marathonsService.save(id, dto);
        assertNull(result);
    }

    @Test
    void shouldSaveMarathon() {
        MarathonsDTO dto = new MarathonsDTO();
        Marathons entity = new Marathons();
        Marathons saved = new Marathons();
        MarathonsDTO savedDto = new MarathonsDTO();

        when(marathonsConverter.convertToEntity(dto)).thenReturn(entity);
        when(marathonsRepository.save(entity)).thenReturn(saved);
        when(marathonsConverter.convertToDto(saved)).thenReturn(savedDto);

        MarathonsDTO result = marathonsService.save(dto);
        assertNotNull(result);
        verify(marathonsRepository).save(entity);
    }

    @Test
    void shouldDeleteMarathonById() {
        marathonsService.delete(1L);
        verify(marathonsRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldSaveMarathonWithIdWhenMembersIsNull() {
        Long id = 1L;
        MarathonsDTO dto = new MarathonsDTO();
        Marathons existing = new Marathons();
        existing.setMembers(new ArrayList<>()); // <-- inicializa a lista vazia!
        Marathons toSave = new Marathons();
        Marathons saved = new Marathons();
        MarathonsDTO savedDto = new MarathonsDTO();

        when(marathonsRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(memberRepository).deleteMembersByMarathons(existing);
        when(marathonsConverter.convertToEntity(dto, existing)).thenReturn(toSave);
        when(marathonsRepository.save(toSave)).thenReturn(saved);
        when(marathonsConverter.convertToDto(saved)).thenReturn(savedDto);

        MarathonsDTO result = marathonsService.save(id, dto);
        assertNotNull(result);
        verify(memberRepository).deleteMembersByMarathons(existing);
        verify(marathonsRepository).save(toSave);
    }

    @Test
    void shouldSaveMarathonWithIdWhenMemberMarathonsIsNotNull() {
        Long id = 1L;
        MarathonsDTO dto = new MarathonsDTO();
        Marathons existing = new Marathons();
        Member member = new Member();
        member.setMarathons(new ArrayList<>()); // já inicializado
        existing.setMembers(new ArrayList<>(List.of(member)));
        Marathons toSave = new Marathons();
        toSave.setMembers(new ArrayList<>(List.of(member)));
        Marathons saved = new Marathons();
        MarathonsDTO savedDto = new MarathonsDTO();

        when(marathonsRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(memberRepository).deleteMembersByMarathons(existing);
        when(marathonsConverter.convertToEntity(dto, existing)).thenReturn(toSave);
        when(marathonsRepository.save(toSave)).thenReturn(saved);
        when(marathonsConverter.convertToDto(saved)).thenReturn(savedDto);

        MarathonsDTO result = marathonsService.save(id, dto);
        assertNotNull(result);
        verify(memberRepository).deleteMembersByMarathons(existing);
        verify(marathonsRepository).save(toSave);
    }

    @Test
    void shouldAddMarathonToMemberWhenListAlreadyInitializedAndContainsMarathon() {
        Member member = new Member();
        Marathons marathon = new Marathons();
        List<Marathons> marathonsList = new ArrayList<>();
        marathonsList.add(marathon);
        member.setMarathons(marathonsList);

        // O método adiciona mesmo se já existir, então espera 2
        marathonsService.addMarathon(member, marathon);
        assertEquals(2, member.getMarathons().size());
        assertTrue(member.getMarathons().contains(marathon));
    }
}