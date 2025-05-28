package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    void shouldSaveGroup() {
        GroupDTO dto = new GroupDTO();
        Group group = new Group();
        when(groupConverter.convertToEntity(dto)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupConverter.convertToDto(group)).thenReturn(dto);
        assertNotNull(groupService.save(dto));
    }

    @Test
    void shouldDeleteGroup() {
        groupService.delete(1L);
        verify(groupRepository, times(1)).deleteById(1L);
    }
}