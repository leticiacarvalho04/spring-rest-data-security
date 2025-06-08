package br.edu.fatecsjc.lgnspringapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiConfigTest {

    @Test
    void shouldHaveOpenAPIDefinitionAnnotation() {
        assertThat(OpenApiConfig.class).hasAnnotation(OpenAPIDefinition.class);
    }

    @Test
    void shouldHaveInfoWithContactAndLicense() {
        OpenAPIDefinition definition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);
        Info info = definition.info();

        assertThat(info.contact()).isNotNull();
        assertThat(info.contact().name()).isEqualTo("Lucas Nadalete");
        assertThat(info.contact().email()).isEqualTo("lucas.nadalete@fatec.sp.gov.br");

        assertThat(info.license()).isNotNull();
        assertThat(info.license().name()).isEqualTo("MIT License");
        assertThat(info.license().url()).isEqualTo("https://opensource.org/license/mit/");
        assertThat(info.description()).isEqualTo("API Documentation for Academic Sample");
        assertThat(info.title()).isEqualTo("OpenApi specification - LGN");
        assertThat(info.version()).isEqualTo("1.0");
        assertThat(info.termsOfService()).isEqualTo("Terms of service");
    }

    @Test
    void shouldHaveServerConfiguration() {
        OpenAPIDefinition definition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);
        Server[] servers = definition.servers();

        assertThat(servers).isNotEmpty();
        assertThat(servers[0].description()).isEqualTo("Local ENV");
        assertThat(servers[0].url()).isEqualTo("http://localhost:8000");
    }

    @Test
    void shouldHaveSecuritySchemeConfiguration() {
        assertThat(OpenApiConfig.class).hasAnnotation(SecurityScheme.class);

        SecurityScheme securityScheme = OpenApiConfig.class.getAnnotation(SecurityScheme.class);

        assertThat(securityScheme.name()).isEqualTo("bearerAuth");
        assertThat(securityScheme.description()).isEqualTo("JWT auth description");
        assertThat(securityScheme.scheme()).isEqualTo("bearer");
        assertThat(securityScheme.type()).isEqualTo(SecuritySchemeType.HTTP);
        assertThat(securityScheme.bearerFormat()).isEqualTo("JWT");
        assertThat(securityScheme.in()).isEqualTo(SecuritySchemeIn.HEADER);
    }

    @Test
    void shouldPingSuccessfully() {
        OpenApiConfig config = new OpenApiConfig();
        assertThat(config.ping()).isEqualTo("pong");
    }
}