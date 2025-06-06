package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private GroupConverter groupConverter;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllGroups() {
        when(groupRepository.findAll()).thenReturn(Collections.singletonList(new Group()));
        when(groupConverter.convertToDto(anyList())).thenReturn(Collections.singletonList(new GroupDTO()));
        assertEquals(1, groupService.getAll().size());
    }

    @Test
    void shouldReturnGroupById() {
        Group group = new Group();
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupConverter.convertToDto(group)).thenReturn(new GroupDTO());
        assertNotNull(groupService.findById(1L));
    }

    @Test
    void shouldThrowExceptionWhenGroupNotFoundById() {
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> groupService.findById(99L));
    }

    @Test
    void shouldSaveGroup() {
        GroupDTO dto = new GroupDTO();
        Group group = new Group();
        when(groupConverter.convertToEntity(dto)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupConverter.convertToDto(group)).thenReturn(dto);
        assertNotNull(groupService.save(dto));
    }

    @Test
    void shouldSaveGroupWithIdWhenExists() {
        Long id = 1L;
        GroupDTO dto = new GroupDTO();
        Group existingGroup = new Group();
        // Adiciona membros usando o getter
        existingGroup.getMembers().add(new Member());
        existingGroup.getMembers().add(new Member());

        // Mock para findById
        when(groupRepository.findById(id)).thenReturn(Optional.of(existingGroup));
        when(groupConverter.convertToEntity(eq(dto), eq(existingGroup))).thenReturn(existingGroup);
        when(groupRepository.save(existingGroup)).thenReturn(existingGroup);
        when(groupConverter.convertToDto(existingGroup)).thenReturn(dto);

        doNothing().when(memberRepository).deleteMembersByGroup(existingGroup);

        GroupDTO result = groupService.save(id, dto);
        assertNotNull(result);
        verify(memberRepository, times(1)).deleteMembersByGroup(existingGroup);
        verify(groupRepository, times(1)).save(existingGroup);
    }

    @Test
    void shouldReturnNullWhenSaveGroupWithIdNotFound() {
        Long id = 2L;
        GroupDTO dto = new GroupDTO();
        when(groupRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(groupService.save(id, dto));
    }

    @Test
    void shouldDeleteGroup() {
        groupService.delete(1L);
        verify(groupRepository, times(1)).deleteById(1L);
    }
}