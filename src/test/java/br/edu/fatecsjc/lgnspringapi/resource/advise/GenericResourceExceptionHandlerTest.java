package br.edu.fatecsjc.lgnspringapi.resource.advise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.edu.fatecsjc.lgnspringapi.dto.ApiErrorDTO;
import br.edu.fatecsjc.lgnspringapi.resource.advice.GenericResourceExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenericResourceExceptionHandlerTest {

    private GenericResourceExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GenericResourceExceptionHandler();
    }

    @Test
    void shouldReturnBadRequest_whenGenericExceptionIsThrown() throws Exception {
        Exception exception = new Exception("Some error");

        ResponseEntity<ApiErrorDTO> response = exceptionHandler.handleGenericException(request, exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("An unknown error occurred in API processing", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldThrowAccessDeniedException_whenAccessDenied() {
        AccessDeniedException accessDeniedException = new AccessDeniedException("Forbidden");

        assertThrows(AccessDeniedException.class, () -> {
            exceptionHandler.handleGenericException(request, accessDeniedException);
        });
    }
}
