package br.edu.fatecsjc.lgnspringapi.resource.advice;

import br.edu.fatecsjc.lgnspringapi.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenericResourceExceptionHandlerTest {

    @Test
    void shouldReturnBadRequestAndApiErrorDTO_onGenericException() throws Exception {
        GenericResourceExceptionHandler handler = new GenericResourceExceptionHandler();
        HttpServletRequest req = mock(HttpServletRequest.class);
        Exception ex = new RuntimeException("Erro genérico");

        ResponseEntity<ApiErrorDTO> response = handler.handleGenericException(req, ex);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("An unknown error occurred in API processing", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldRethrowAccessDeniedException() {
        GenericResourceExceptionHandler handler = new GenericResourceExceptionHandler();
        HttpServletRequest req = mock(HttpServletRequest.class);
        AccessDeniedException ex = new AccessDeniedException("Acesso negado");

        assertThrows(AccessDeniedException.class, () -> handler.handleGenericException(req, ex));
    }

    @Test
    void catchGenericExceptionShouldDelegateToHandleGenericException() throws Exception {
        GenericResourceExceptionHandler handler = new GenericResourceExceptionHandler();
        HttpServletRequest req = mock(HttpServletRequest.class);
        Exception ex = new RuntimeException("Erro");

        ResponseEntity<ApiErrorDTO> response = handler.catchGenericException(req, ex);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}