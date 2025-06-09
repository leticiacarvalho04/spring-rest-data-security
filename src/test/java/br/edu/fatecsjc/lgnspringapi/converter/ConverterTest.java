package br.edu.fatecsjc.lgnspringapi.converter;

import org.junit.jupiter.api.Test;
import java.util.List;

import br.edu.fatecsjc.lgnspringapi.converter.Converter;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterTest {
	@Test
    void testConverterInterfaceMethodsExist() {
        Class<?> clazz = Converter.class;
        
        assertDoesNotThrow(() -> clazz.getMethod("convertToEntity", Object.class));
        assertDoesNotThrow(() -> clazz.getMethod("convertToEntity", Object.class, Object.class));
        assertDoesNotThrow(() -> clazz.getMethod("convertToDto", Object.class));
        assertDoesNotThrow(() -> clazz.getMethod("convertToEntity", List.class));
        assertDoesNotThrow(() -> clazz.getMethod("convertToDto", List.class));
    }
}
