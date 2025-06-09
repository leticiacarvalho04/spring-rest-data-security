package br.edu.fatecsjc.lgnspringapi.resource.advice;

import br.edu.fatecsjc.lgnspringapi.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GenericResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(HttpServletRequest req, Exception exception) throws Exception {
        if (exception instanceof AccessDeniedException) {
            throw (AccessDeniedException) exception;
        }
        
        ApiErrorDTO error = ApiErrorDTO.builder()
                .message("An unknown error occurred in API processing")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    public ResponseEntity<ApiErrorDTO> catchGenericException(HttpServletRequest req, Exception exception) throws Exception {
        return handleGenericException(req, exception);
    }

}
