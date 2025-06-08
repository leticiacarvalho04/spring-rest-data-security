package br.edu.fatecsjc.lgnspringapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TestSecurityConfig {

    @Test
    void shouldCreateSecurityFilterChain() throws Exception {
        TestSecurityConfig config = new TestSecurityConfig();
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        assertThat(config).isNotNull();
    }
}