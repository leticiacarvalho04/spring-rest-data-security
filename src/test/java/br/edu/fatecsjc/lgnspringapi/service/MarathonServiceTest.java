package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.repository.MarathonsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class MarathonsServiceTest {

    @Mock
    private MarathonsRepository marathonsRepository;

    @InjectMocks
    private MarathonsService marathonsService;

    @Test
    void shouldDeleteMarathonById() {
        MockitoAnnotations.openMocks(this);
        marathonsService.delete(1L);
        verify(marathonsRepository, times(1)).deleteById(1L);
    }
}