package br.edu.fatecsjc.lgnspringapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NamingConventions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class ModelMapperConfigTest {
    
    private ModelMapperConfig modelMapperConfig;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        modelMapperConfig = new ModelMapperConfig();
    }

    @Test
    void testModelMapperConfiguration() {
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        assertNotNull(modelMapper);

        assertTrue(modelMapper.getConfiguration().isFieldMatchingEnabled());
        assertEquals(Configuration.AccessLevel.PRIVATE, modelMapper.getConfiguration().getFieldAccessLevel());
        assertEquals(NamingConventions.JAVABEANS_MUTATOR, modelMapper.getConfiguration().getSourceNamingConvention());
    }
}
