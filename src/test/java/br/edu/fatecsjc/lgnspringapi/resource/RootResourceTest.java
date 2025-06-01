package br.edu.fatecsjc.lgnspringapi.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class RootResourceTest {

    private RootResource rootResource;

    @Value("${server.port}")
    private String port;

    @BeforeEach
    void setUp() {
        rootResource = new RootResource();
        
        try {
            var field = RootResource.class.getDeclaredField("port");
            field.setAccessible(true);
            field.set(rootResource, port);
        } catch (Exception e) {
            fail("Erro ao injetar valor da porta: " + e.getMessage());
        }
    }

    @Test
    void shouldReturnWelcomeMessageWithSwaggerLink() {
        // Act
        ResponseEntity<String> response = rootResource.validateRestResource();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Bem-vindo, APIs operacionais."));
        assertTrue(response.getBody().contains("http://localhost:" + port + "/swagger-ui/index.html"));
    }

    @Test
    void shouldNotHaveEmptyBody() {
        ResponseEntity<String> response = rootResource.validateRestResource();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}